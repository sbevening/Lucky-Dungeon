package ui;

import ui.Game;
import ui.GamePanel;
import ui.Refreshable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// panel to allow player to attack the enemy by pressing a button and to manage the flow of battle
// and end game if needed
public class BattlePanel extends JPanel implements Refreshable {
    private static final Color BACKGROUND_COLOR = new Color(200, 200, 200);
    private static final Color BORDER_COLOR = Color.CYAN;

    private GamePanel gamePanel;
    private Game game;

    // EFFECTS: constructs a panel to represent combat aspects of a game belonging to a given gamePanel
    // and a button to attack the enemy
    public BattlePanel(GamePanel gamePanel) {
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        this.gamePanel = gamePanel;
        game = gamePanel.getGame();
        JButton jb = new JButton("ATTACK ENEMY");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doAttack();
            }
        });
        add(jb);
    }

    // MODIFIES: this
    // EFFECTS: makes player attack the enemy, selects new enemy if current one is killed, makes enemy attack player if
    // it is still alive, and ends game if player dies. relevant information is also displayed by gamePanel.
    private void doAttack() {
        String enemyName = game.getCurrentEnemy().getName();

        game.makePlayerHit();
        if (game.getCurrentEnemy().getHitPoints() <= 0) {
            gamePanel.displayMessage(enemyName + " was slain!");
            gamePanel.displayMessage(game.collectLoot());
            gamePanel.displayMessage(game.selectEnemy() + " appeared!");
        } else {
            int enemyHitPoints = game.getCurrentEnemy().getHitPoints();
            gamePanel.displayMessage("You attacked " + enemyName + ", reducing its health to " + enemyHitPoints);
            gamePanel.displayMessage(game.makeEnemyMove());
        }
        if (game.getPlayer().getEquippedWeapon() == null) {
            gamePanel.displayMessage("YOUR WEAPON IS BROKEN!!!");
        }
        if (game.getPlayer().getHitPoints() <= 0) {
            gamePanel.endGame();
        }
        gamePanel.refresh();
    }

    // MODIFIES: this
    // EFFECTS: refreshes this panel with current game information
    @Override
    public void refresh() {
        game = gamePanel.getGame();
    }
}
