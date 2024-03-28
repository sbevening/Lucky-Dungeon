package ui;

import model.Enemy;

// panel to visually display information for the current enemy in a game contained in a GamePanel
public class CurrentEnemyInformationPanel extends EntityInformationPanel {
    private Enemy enemy;
    private final GamePanel gamePanel;

    // EFFECTS: constructs a CurrentEnemyInformationPanel that gets information from a given GamePanel
    public CurrentEnemyInformationPanel(GamePanel gamePanel) {
        super(gamePanel.getGame().getCurrentEnemy());
        this.gamePanel = gamePanel;
        this.enemy = gamePanel.getGame().getCurrentEnemy();
        refresh();
    }

    // MODIFIES: this
    // EFFECTS: updates this panel to have information on current state of the current enemy
    @Override
    public void refresh() {
        enemy = gamePanel.getGame().getCurrentEnemy();
        super.setEntity(enemy);
        super.refresh();
        removeAll();
        addInformationText();
        validate();
        updateUI();
    }
}
