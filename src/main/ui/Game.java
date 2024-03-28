package ui;

import model.Player;
import model.Enemy;
import model.Weapon;
import model.Armor;
import model.Item;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// class to represent mechanics of the game and actively output game information to console
public class Game {
    private static final String JSON_STORE = "./data/game.json";
    private Player player;
    private Enemy currentEnemy;
    private final ArrayList<Enemy> enemyList;

    private static final int START_HITPOINTS = 20;
    private static final int INVENTORY_SLOTS = 6;

    // EFFECTS: prompts user to start game; instantiates game and initializes starting fields in it
    public Game() {
        player = new Player("Player", START_HITPOINTS, INVENTORY_SLOTS);
        EnemyListGenerator enemyListGenerator = new EnemyListGenerator();
        enemyList = enemyListGenerator.getEnemyList();
        selectEnemy();
    }

    // MODIFIES: this
    // EFFECTS: selects random enemy from list of all enemies, generates shallow copy of it,
    // and assigns it to currentEnemy. also returns name of that entity
    public String selectEnemy() {
        int randomIndex = (int) (Math.random() * enemyList.size());
        // enemies can be reused so make a template and shallow copy
        Enemy enemyTemplate = enemyList.get(randomIndex);
        String name = enemyTemplate.getName();
        int hp = enemyTemplate.getHitPoints();
        int atk = enemyTemplate.getAttack();
        int def = enemyTemplate.getDefence();
        Item[] loot = enemyTemplate.getLoot();
        currentEnemy = new Enemy(name, hp, def, atk, loot);
        return currentEnemy.getName();
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    // MODIFIES: this
    // EFFECTS: has the current enemy attack the player and reduce its hitpoints.
    // returns string  containing information on the damage dealt.
    public String makeEnemyMove() {
        currentEnemy.hitEntity(player);
        int newHitpoints = player.getHitPoints();
        return currentEnemy.getName() + " attacks! Your hitpoints drop to " + newHitpoints;
    }

    // MODIFIES: this
    // EFFECTS: loads player and enemy states from given file if file can be opened. returns true if file was
    // successfully read from; false otherwise
    public boolean loadGame() {
        JsonReader reader = new JsonReader(JSON_STORE);
        try {
            reader.generatePlayerAndEnemy();
            player = reader.getPlayer();
            currentEnemy = reader.getEnemy();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: saves player and enemy as json to destination file. returns true if save was successful, false if
    // a FileNotFoundException occurs
    public Boolean saveGame() {
        try {
            JsonWriter writer = new JsonWriter(JSON_STORE);
            writer.initializeWriter();
            writer.write(player, currentEnemy);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: makes player attack current enemy and decreases their health if a weapon is equipped
    // and returns information string on attack. if no weapon is equipped, returns a no damage dealt message.
    public String makePlayerHit() {
        String messageOut;
        Weapon equippedWeapon = player.getEquippedWeapon();

        if (equippedWeapon == null) {
            messageOut = "You punch " + currentEnemy.getName() + "! It didn't do much...";
            return messageOut;
        } else if (player.hitEntity(currentEnemy)) {
            messageOut = "You attack and slay " + currentEnemy.getName() + "!";
        } else {
            messageOut = "You attack " + currentEnemy.getName() + ", reducing their health to "
                    + currentEnemy.getHitPoints() + "\n";
        }

        if (equippedWeapon.isOutOfUses()) {
            messageOut += "\nYour " + equippedWeapon.getName() + " broke!";
            player.equipWeapon(null);
        }

        return messageOut;
    }

    // REQUIRES: loot is not null, loot.length > 0
    // MODIFIES: this
    // EFFECTS: attempts to pick up loot. returns string report of loot found.
    public String collectLoot() {
        StringBuilder outputString = new StringBuilder();
        for (Item item : currentEnemy.getLoot()) {
            if (player.getInventory().pickUpItem(item)) {
                // if item is successfully picked up
                outputString.append("You found a ").append(item.getName()).append(" and add it to your inventory. ");
            } else {
                // if no space for item
                outputString.append(item.getName()).append(" was dropped but your inventory is full. ");
            }
        }

        return outputString.toString();
    }

    // MODIFIES: this
    // EFFECTS: prompts user to equipped different weapon in inventory and equips a chosen weapon
    // if the input given is valid. if no weapons in inventory, logs no weapon message to console and
    // does not swap
    public void swapWeapon(Weapon selectedWeapon) {
        player.equipWeapon(selectedWeapon);
    }

    // MODIFIES: this
    // EFFECTS: prompts user to equipped different armor in inventory and equips chosen armor
    // if the input given is valid. if no weapons in inventory, logs no weapon message to console and
    // does not swap
    public void swapArmor(Armor selectedArmor) {
        player.equipArmor(selectedArmor);
    }

    // MODIFIES: this
    // EFFECTS: removes given weapon from inventory
    public void dropItem(Item item) {
        player.getInventory().dropItem(item);
    }
}
