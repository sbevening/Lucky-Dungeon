package ui;

import model.Armor;
import model.Player;
import model.Weapon;

import javax.swing.*;

// Class to visually represent a player instance within a JPanel
public class PlayerInformationPanel extends EntityInformationPanel {
    private Player player;
    private final GamePanel gamePanel;
    private Game game;

    // EFFECTS: constructs panel that represents the player contained in a GamePanel's game
    public PlayerInformationPanel(GamePanel gamePanel) {
        super(gamePanel.getGame().getPlayer());
        this.gamePanel = gamePanel;
        this.game = gamePanel.getGame();
        this.player = game.getPlayer();
        refresh(); // creates panel with initial fields
    }

    // MODIFIES: this
    // EFFECTS: updates this panel to provide information on current player attached to gamePanel
    // and its state
    @Override
    public void refresh() {
        game = gamePanel.getGame();
        player = game.getPlayer();
        super.setEntity(player);
        removeAll();
        addInformationText();

        Weapon equippedWeapon = player.getEquippedWeapon();
        if (equippedWeapon != null) {
            add(new JLabel("equipped weapon: " + equippedWeapon.getName()));
            add(new JLabel(" -> uses left: " + equippedWeapon.getUsesRemaining()));
        } else {
            add(new JLabel("no weapon equipped"));
        }

        Armor equippedArmor = player.getEquippedArmor();
        if (equippedArmor != null) {
            add(new JLabel("equipped armor: " + equippedArmor.getName()));
        } else {
            add(new JLabel("no armor equipped"));
        }

        validate();
        updateUI();
    }
}
