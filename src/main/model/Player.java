package model;

import org.json.JSONObject;

// player class used to represent state of player in game with given hitpoints, inventory,
// and equipped gear
public class Player extends Entity {
    private Armor equippedArmor;
    private Weapon equippedWeapon;
    private Inventory inventory;

    // REQUIRES: hp > 0 and inventorySlots > 0
    // EFFECTS: creates a player instance with given name, hitpoints, given
    // inventory slots, and basic armor and weapon
    public Player(String name, int hp, int inventorySlots) {
        // attack is set to Dagger's attack; defence is set to T-Shirt's defence
        super(name, hp, 0, 2);
        equippedArmor = new Armor("T-Shirt", 0);
        equippedWeapon = new Weapon("Dagger", 2, 10);
        inventory = new Inventory(inventorySlots);
    }

    // REQUIRES: hp > 0
    // EFFECTS: creates a player instance with given name, hitpoints, inventory,
    // null equipped weapon, and basic armor
    public Player(String name, int hp, Inventory inventory) {
        // attack is set to 0 and defence is set to 0 because nothing equipped
        super(name, hp, 0, 0);
        equippedArmor = new Armor("T-Shirt", 0);
        equippedWeapon = null;
        this.inventory = inventory;
    }

    public Armor getEquippedArmor() {
        return equippedArmor;
    }

    // REQUIRES: armor != null
    // MODIFIES: this
    // EFFECTS: sets equipped armor to given armor piece and defence to armor's defence with no
    // interactions with inventory. overwrites previous armor and does not add it to inventory.
    public void setEquippedArmor(Armor armor) {
        equippedArmor = armor;
        setDefence(armor.getDefence());
    }

    // REQUIRES: armor is in inventory.getItems()
    // MODIFIES: this
    // EFFECTS: sets equipped armor to given armor and removes it from inventory;
    // adds previously worn armor to inventory; sets defence to newly equipped
    // armor's defence value
    public void equipArmor(Armor armor) {
        Armor previousArmor = equippedArmor;
        equippedArmor = armor;
        inventory.dropItem(armor);
        inventory.pickUpItem(previousArmor);
        setDefence(armor.getDefence());
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    // MODIFIES: this
    // EFFECTS: sets equipped weapon to given weapon with no interactions with
    // inventory. overwrites previous weapon and does not add it to inventory.
    // sets attack to weapon's attack or 0 if weapon is null
    public void setEquippedWeapon(Weapon weapon) {
        equippedWeapon = weapon;
        if (weapon == null) {
            setAttack(0);
        } else {
            setAttack(weapon.getAttack());
        }
    }

    // REQUIRES: weapon is in inventory.getItems()
    // MODIFIES: this
    // EFFECTS: if weapon != null, sets equipped weapon to given weapon and removes
    // it from inventory; adds previously worn weapon to inventory; sets attack to
    // newly equipped weapon's attack value. if weapon is null, sets player's attack to
    // 0, sets equippedWeapon to null, and destroys/overwrites previously held item
    // (used for weapons breaking).
    public void equipWeapon(Weapon weapon) {
        if (weapon == null) {
            setAttack(0);
            equippedWeapon = null;
        } else {
            Weapon previousWeapon = equippedWeapon;
            equippedWeapon = weapon;
            inventory.dropItem(weapon);
            inventory.pickUpItem(previousWeapon);
            setAttack(weapon.getAttack());
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    // EFFECTS: returns string representation of player and all of its relevant fields
    @Override
    public String toString() {
        String weaponString;
        String hpString = "\nHitpoints: " + Integer.toString(getHitPoints());
        if (equippedWeapon != null) {
            weaponString = "\nEquipped Weapon: " + getEquippedWeapon().toString();
        } else {
            weaponString = "\nNo weapon equipped";
        }
        String armorString = "\nEquipped Armor: " + getEquippedArmor().toString();
        return "\nPlayer Stats" + hpString + weaponString + armorString;
    }

    // MODIFIES: other
    // EFFECTS: attacks other entity with equipped weapon. returns true if other dies; returns false otherwise
    @Override
    public Boolean hitEntity(Entity other) {
        if (equippedWeapon == null) {
            return false;
        }
        Boolean enemyDied = super.hitEntity(other);
        equippedWeapon.decrementUses();
        return enemyDied;
    }

    // EFFECTS: produces json representation of player
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = super.toJson();
        jsonObject.put("equippedArmor", equippedArmor.toJson());
        if (equippedWeapon != null) {
            jsonObject.put("equippedWeapon", equippedWeapon.toJson());
        } else {
            jsonObject.put("equippedWeapon", JSONObject.NULL);
        }
        jsonObject.put("inventory", inventory.toJson());

        return jsonObject;
    }
}
