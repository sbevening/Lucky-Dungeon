package model;

// Abstract class for an item to be held within an Inventory
public abstract class Item {
    protected String name;

    // REQUIRES: name is a non-empty string
    // EFFECTS: creates Item with given name
    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public abstract String toString();
}
