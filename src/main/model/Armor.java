package model;

// armor to be equipped by player or stored in armor inventory
// that absorbs damage in combat
public class Armor extends Item {
    private int defence;

    // REQUIRES: name is a non-empty string; defence >= 0
    // EFFECTS: creates a new Armor instance with given
    // name and defence
    public Armor(String name, int defence) {
        super(name);
        this.defence = defence;
    }

    public int getDefence() {
        return defence;
    }

    // EFFECTS: returns string representation of armor
    @Override
    public String toString() {
        return name + " - defence: " + Integer.toString(defence);
    }
}
