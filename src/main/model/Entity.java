package model;

import org.json.JSONArray;
import org.json.JSONObject;

// abstract class for any entity in the game (player or enemy) with given
// battle stats and name
public abstract class Entity {
    private String name;
    private int hitPoints;
    private int defence;
    private int attack;

    // REQUIRES: hp >= 1, atk >= 0, and def >= 0
    // EFFECTS: creates Entity instance with given hitPoints,
    // defense, and attack values
    public Entity(String name, int hp, int def, int atk) {
        this.name = name;
        hitPoints = hp;
        defence = def;
        attack = atk;
    }

    // REQUIRES: incomingAttack >= 0
    // MODIFIES: this
    // EFFECTS: no change made if incomingDamage <= defence. else, reduces hitPoints
    // by (incomingDamage - defence). if hitPoints are then below 0, they are
    // instead set to 0 and returns true; otherwise, returns false
    public Boolean takeHit(int incomingDamage) {
        if (incomingDamage > defence) {
            int newHitPoints = hitPoints - (incomingDamage - defence);
            if (newHitPoints <= 0) {
                hitPoints = 0;
                return true;
            } else {
                hitPoints = newHitPoints;
                return false;
            }
        } else {
            return false;
        }
    }

    // MODIFIES: other
    // EFFECTS: attacks another entity and makes it take a hit
    // with damage equal to their attack. returns true if other
    // entity's hitPoints fall to 0
    public Boolean hitEntity(Entity other) {
        return other.takeHit(attack);
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getDefence() {
        return defence;
    }

    // REQUIRES: defence >= 0
    // MODIFIES: this
    // EFFECTS: sets defence to given value
    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getAttack() {
        return attack;
    }

    // REQUIRES: attack > 0
    // MODIFIES: this
    // EFFECTS: sets attack to given value
    public void setAttack(int attack) {
        this.attack = attack;
    }

    public String getName() {
        return name;
    }

    @Override
    public abstract String toString();

    // EFFECTS: returns JSON representation of this entity
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("hitPoints", hitPoints);
        jsonObject.put("defence", defence);
        jsonObject.put("attack", attack);

        return jsonObject;
    }
}
