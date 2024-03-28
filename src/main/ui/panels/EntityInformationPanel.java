package ui.panels;

import model.Entity;

import javax.swing.*;
import java.awt.*;

// abstract class to visually represent an entity instance within a JPanel
public abstract class EntityInformationPanel extends JPanel implements Refreshable {
    private static final Color BACKGROUND_COLOR = new Color(200, 200, 200);
    private Entity entity;

    // EFFECTS: constructs a panel with information on given entity
    public EntityInformationPanel(Entity entity) {
        this.entity = entity;
        setPreferredSize(new Dimension(200, 100));
        setMaximumSize(new Dimension(200, 100));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(Color.cyan, 2));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addInformationText();
    }

    // MODIFIES: this
    // EFFECTS: sets this panels entity to a given entity
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    // MODIFIES: this
    // EFFECTS: adds labels containing information on entity to panel
    protected void addInformationText() {
        add(new JLabel(entity.getName().toUpperCase()));
        add(new JLabel("hitpoints: " + entity.getHitPoints()));
        add(new JLabel("attack: " + entity.getAttack()));
        add(new JLabel("defence: " + entity.getDefence()));
    }

    // MODIFIES: this
    // EFFECTS: updates panel to show current entity information
    @Override
    public void refresh() {
        removeAll();
        addInformationText();
        validate();
        updateUI();
    }
}
