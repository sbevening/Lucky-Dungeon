package ui.panels;

import model.EventLog;
import model.LogPrinter;
import ui.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

// class to create a JPanel visually representing the entire game and controlling its general flow
public class GamePanel extends JPanel implements Refreshable {
    private static final int NUM_MESSAGES_DISPLAYED = 6;

    private Game game;
    private InventoryPanel inventoryPanel;
    private PersistencePanel persistencePanel;
    private PlayerInformationPanel playerInformationPanel;
    private EntityInformationPanel enemyInformationPanel;
    private BattlePanel battlePanel;
    private GameMessagesPanel gameMessagesPanel;

    // REQUIRES: panelWidth > 0; panelHeight > 0
    // EFFECTS: constructs a JPanel to represent a given game
    public GamePanel(Game game) {
        this.game = game;
        generatePanel();
    }

    // MODIFIES: this
    // EFFECTS: generates the sub-panels for each part of the gui and sets layout of this panel
    private void generatePanel() {
        setLayout(new BorderLayout());

        inventoryPanel = new InventoryPanel(this);
        persistencePanel = new PersistencePanel(this);
        playerInformationPanel = new PlayerInformationPanel(this);
        enemyInformationPanel = new CurrentEnemyInformationPanel(this);
        battlePanel = new BattlePanel(this);
        gameMessagesPanel = new GameMessagesPanel(NUM_MESSAGES_DISPLAYED);

        JPanel westPanel = new JPanel();
        westPanel.setLayout(new GridLayout(3, 1));
        westPanel.add(playerInformationPanel);
        westPanel.add(persistencePanel);
        westPanel.add(battlePanel);
        add(westPanel, BorderLayout.WEST);
        add(inventoryPanel, BorderLayout.NORTH);
        add(enemyInformationPanel, BorderLayout.EAST);
        add(gameMessagesPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: refreshes the refreshable panels that make up this
    @Override
    public void refresh() {
        inventoryPanel.refresh();
        playerInformationPanel.refresh();
        enemyInformationPanel.refresh();
        gameMessagesPanel.refresh();
    }

    public Game getGame() {
        return game;
    }

    // MODIFIES: this
    // EFFECTS: Generates game over screen and button to restart game
    public void endGame() {
        removeAll();
        setLayout(new GridLayout(2, 1));
        Label gameOverLabel = new Label("Game Over!!!");
        add(gameOverLabel);

        JButton restartButton = new JButton("Start New Game");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game = new Game();
                removeAll();
                generatePanel();
                validate();
                updateUI();
            }
        });
        add(restartButton);
        validate();
        updateUI();
    }

    // EFFECTS: adds a given message to the game messages panel and pushes out
    // the oldest message if at capacity
    public void displayMessage(String message) {
        gameMessagesPanel.addMessage(message);
    }
}
