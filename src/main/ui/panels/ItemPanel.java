package ui.panels;

import model.Armor;
import model.Item;
import model.Weapon;
import ui.StylingUtilities;

import javax.swing.*;
import java.awt.*;

// Class to visually represent an item instance within a JPanel
public class ItemPanel extends JPanel {
    private static final int WIDTH = 125;
    private static final int HEIGHT = 125;
    private static final Color SELECTED_COLOR = Color.green;

    private Item item;
    private Boolean selected = false;

    // EFFECTS: Constructs JPanel for given item or empty panel if null
    public ItemPanel(Item item) {
        this.item = item;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(StylingUtilities.createBorderWithMargin(Color.black, 2, 2));
        if (selected) {
            setBackground(SELECTED_COLOR);
        }
        setVisible(true);

        if (item != null) {
            drawItem();
        } else {
            add(new JLabel("Empty"));
        }
    }

    // MODIFIES: this
    // EFFECTS: makes panel represent give not-null item
    private void drawItem() {
        add(new JLabel(item.getName().toUpperCase()));
        if (item instanceof Weapon) {
            Weapon weapon = (Weapon) item;
            add(new JLabel("Attack: " + weapon.getAttack()));
            add(new JLabel("Uses Left: " + weapon.getUsesRemaining()));
        } else {
            Armor armor = (Armor) item;
            add(new JLabel("Defence: " + armor.getDefence()));
            add(new JLabel("")); // makes spacing the same as for a weapon
        }
    }

    // MODIFIES: this
    // EFFECTS: sets selected to true or false and changes background colour accordingly
    public void setSelected(Boolean selected) {
        this.selected = selected;
        setBackground(selected ? SELECTED_COLOR : Color.white);
    }

    public Item getItem() {
        return item;
    }
}
