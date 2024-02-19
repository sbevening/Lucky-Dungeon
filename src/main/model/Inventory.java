package model;

import java.util.ArrayList;

// inventory containing a list of weapons and armor collected
// and not equipped, with given size up to 10 items
public class Inventory {
    private int slots;
    private ArrayList<Item> items;

    // REQUIRES: 1 <= slots <= 10
    // EFFECTS: creates inventory with set amount of inventory
    // slots and no items
    public Inventory(int slots) {
        this.slots = slots;
        items = new ArrayList<Item>();
    }

    public int getSlots() {
        return slots;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    // EFFECTS: returns arraylist of all weapons in inventory
    public ArrayList<Weapon> getWeapons() {
        ArrayList<Weapon> weaponsSoFar = new ArrayList<Weapon>();
        for (Item item : items) {
            if (item instanceof Weapon) {
                weaponsSoFar.add((Weapon) item);
            }
        }

        return weaponsSoFar;
    }

    // EFFECTS: returns arraylist of all armor in inventory
    public ArrayList<Armor> getArmor() {
        ArrayList<Armor> armorSoFar = new ArrayList<Armor>();
        for (Item item : items) {
            if (item instanceof Armor) {
                armorSoFar.add((Armor) item);
            }
        }

        return armorSoFar;
    }

    // REQUIRES: index < items.size() and items.size() >= 1
    // EFFECTS: returns weapon at given index in weaponList
    public Item getItemAt(int index) {
        return items.get(index);
    }

    // MODIFIES: this
    // EFFECTS: if there is enough space (items.size() < slots), adds an item to inventory and
    // returns true; otherwise, returns false and does not modify this
    public boolean pickUpItem(Item item) {
        if (items.size() >= slots) {
            return false;
        } else {
            items.add(item);
            return true;
        }
    }

    // REQUIRES: item being removed is in items
    // MODIFIES: this
    // EFFECTS: removes first copy of given weapon from items
    // (item in list is compared to item parameter by Object.equals)
    public void dropItem(Item item) {
        items.remove(item);
    }

    // EFFECTS: returns string representation of inventory. if empty,
    // returns empty inventory message.
    @Override
    public String toString() {
        if (items.size() == 0) {
            return "Inventory is empty.";
        }

        String outputString = "";
        for (Item item : items) {
            outputString += item.toString();
            outputString += "\n";
        }

        return outputString;
    }
}
