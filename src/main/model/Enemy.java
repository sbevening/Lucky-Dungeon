package model;

import org.json.JSONArray;
import org.json.JSONObject;

public class Enemy extends Entity {
    private Item[] loot;

    // REQUIRES: name is non-empty, hp >= 1, atk >= 1, def >= 0
    // EFFECTS: creates an Enemy instance with given name, hp, defence,
    // attack, and loot dropped upon death
    public Enemy(String name, int hp, int def, int atk, Item[] loot) {
        super(name, hp, def, atk);
        this.loot = loot;
    }

    // EFFECTS: returns true if this Enemy drops any loot; otherwise false
    public Boolean dropsLoot() {
        return loot.length > 0;
    }

    public Item[] getLoot() {
        return loot;
    }

    // EFFECTS: returns a string representation of enemy and all its relevant fields
    @Override
    public String toString() {
        String nameString = "\nName: " + getName();
        String hpString = "\nHitpoints: " + getHitPoints();
        String attackString = "\nAttack: " + getAttack();
        String defenceString = "\nDefence: " + getDefence();
        return "\nEnemy Stats" + nameString + hpString + attackString + defenceString;
    }

    // EFFECTS: produces json representation of enemy
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = super.toJson();
        JSONArray lootJson = new JSONArray();
        for (Item item : loot) {
            lootJson.put(item.toJson());
        }
        jsonObject.put("loot", lootJson);
        return jsonObject;
    }
}
