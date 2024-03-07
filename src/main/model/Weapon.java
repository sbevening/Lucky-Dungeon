package model;

import org.json.JSONObject;

// a weapon that can be used against enemy entities or stored in
// weapon inventory
public class Weapon extends Item {
    private int attack;
    private int usesRemaining;

    // REQUIRES: name is a non-empty string, attack > 0, and uses > 0
    // EFFECTS: creates an instance of the weapon class with a given
    // name, attack value, and amount of times it can be used
    public Weapon(String name, int attack, int uses) {
        super(name);
        this.attack = attack;
        usesRemaining = uses;
    }

    public int getAttack() {
        return attack;
    }

    public int getUsesRemaining() {
        return usesRemaining;
    }

    // REQUIRES: usesRemaining >= 1
    // MODIFIES: this
    // EFFECTS: decrements weapon uses left by 1. returns false
    // if usesRemaining reaches 0; returns true otherwise.
    public boolean decrementUses() {
        usesRemaining--;
        return usesRemaining > 0;
    }

    // EFFECTS: returns true if usesRemaining <= 0; otherwise
    // returns false
    public boolean isOutOfUses() {
        return usesRemaining <= 0;
    }

    // EFFECTS: returns string representation of weapon
    @Override
    public String toString() {
        return name + " - attack: " + Integer.toString(attack) + ". uses left: " + Integer.toString(usesRemaining);
    }

    // EFFECTS: produces string representation of weapon
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itemType", "weapon");
        jsonObject.put("name", name);
        jsonObject.put("attack", attack);
        jsonObject.put("usesRemaining", usesRemaining);
        return jsonObject;
    }
}
