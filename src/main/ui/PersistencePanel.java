package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Class to provide JPanel graphical interface to save and load given game to file
public class PersistencePanel extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(200, 200, 200);
    private Game game;

    // EFFECTS: constructs gui to save and load game
    public PersistencePanel(GamePanel gamePanel) {
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(Color.cyan, 2));
        game = gamePanel.getGame();

        JButton saveButton = new JButton("Save Game");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.saveGame();
                gamePanel.displayMessage("Game Saved.");
                gamePanel.refresh();
            }
        });
        add(saveButton);

        JButton loadButton = new JButton("Load Game");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.loadGame();
                gamePanel.displayMessage("Game Loaded.");
                gamePanel.refresh();
            }
        });
        add(loadButton);
    }
}
