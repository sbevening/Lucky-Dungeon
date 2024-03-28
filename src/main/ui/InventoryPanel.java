package ui;

import model.Armor;
import model.Inventory;
import model.Item;
import model.Weapon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Class to visually represent an inventory instance within a JPanel
public class InventoryPanel extends JPanel implements Refreshable {
    private static final Color BACKGROUND_COLOR = new Color(200, 200, 200);
    private static final Color BORDER_COLOR = Color.CYAN;
    private Inventory inventory;
    private GamePanel gamePanel;
    private Game game;
    private ItemPanel selectedItemPanel;
    private JPanel itemsPanel;

    // MODIFIES: this
    // EFFECTS: constructs inventory panel of given size and background colour, linked to
    // given game
    public InventoryPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.game = gamePanel.getGame();
        this.inventory = game.getPlayer().getInventory();
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));

        itemsPanel = new JPanel();
        itemsPanel.add(drawItems());
        add(itemsPanel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(generateDropItemButton());
        buttonsPanel.add(generateEquipItemButton());
        buttonsPanel.setLayout(new GridLayout(2, 1));
        add(buttonsPanel);

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: returns panel containing information about all items in inventory that can be clicked on
    // and selected
    private JPanel drawItems() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        ArrayList<Item> items = inventory.getItems();
        drawOccupiedSlots(panel, items);

        int numEmptySlots = inventory.getSlots() - items.size();
        for (int i = 0; i < numEmptySlots; i++) {
            JPanel emptySlot = new ItemPanel(null);
            panel.add(emptySlot);
        }

        return panel;
    }

    private void drawOccupiedSlots(JPanel panel, ArrayList<Item> items) {
        for (Item item : items) {
            ItemPanel itemPanel = new ItemPanel(item);
            itemPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (itemPanel == selectedItemPanel) {
                        itemPanel.setSelected(false);
                        selectedItemPanel = null;
                        return;
                    }
                    if (selectedItemPanel != null) {
                        selectedItemPanel.setSelected(false);
                    }
                    selectedItemPanel = itemPanel;
                    itemPanel.setSelected(true);
                }
            });
            panel.add(itemPanel);
        }
    }

    // EFFECTS: generates button that drops selected item from inventory and updates panel
    public JButton generateDropItemButton() {
        JButton dropItemButton = new JButton("Drop Item");
        dropItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedItemPanel != null) {
                    Item selectedItem = selectedItemPanel.getItem();
                    game.dropItem(selectedItem);
                    gamePanel.displayMessage("You dropped " + selectedItem.getName());
                    selectedItemPanel.setSelected(false);
                    selectedItemPanel = null;
                    refresh();
                }
            }
        });
        return dropItemButton;
    }

    // EFFECTS: generates button that equips selected item from inventory and updates panel
    public JButton generateEquipItemButton() {
        JButton equipItemButton = new JButton("Equip Item");
        equipItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedItemPanel == null) {
                    return;
                }
                Item selectedItem = selectedItemPanel.getItem();

                if (selectedItem instanceof Armor) {
                    game.swapArmor((Armor) selectedItem);
                } else {
                    game.swapWeapon((Weapon) selectedItem);
                }
                gamePanel.displayMessage("You equipped " + selectedItem.getName());
                selectedItemPanel.setSelected(false);
                selectedItemPanel = null;
                gamePanel.refresh();
            }
        });
        return equipItemButton;
    }

    // MODIFIES: this
    // EFFECTS: refreshes item panel to reflect current inventory state
    public void refresh() {
        game = gamePanel.getGame();
        inventory = game.getPlayer().getInventory();
        itemsPanel.removeAll();
        itemsPanel.add(drawItems());
        validate();
        updateUI();
    }
}
