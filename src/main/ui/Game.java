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
import java.util.Scanner;

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
        String titleString = "\nWELCOME TO LUCKY DUNGEON\n\nAre you ready to start exploring?\nPress enter to start!";
        System.out.println(titleString);
        takeScannerInput();
        System.out.println("You enter a really creepy dungeon... \n\n");
        player = new Player("Player", START_HITPOINTS, INVENTORY_SLOTS);
        EnemyListGenerator enemyListGenerator = new EnemyListGenerator();
        enemyList = enemyListGenerator.getEnemyList();
        selectEnemy();
    }

    private String takeScannerInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    // MODIFIES: this
    // EFFECTS: selects random enemy from list of all enemies, generates shallow copy of it,
    // and assigns it to currentEnemy. also returns name of that entity
    private String selectEnemy() {
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

    // MODIFIES: this
    // EFFECTS: starts the game loop. gives game over message
    // once player drops to 0 health and calls run on new instance
    // of game to start fresh game loop.
    public void run() {
        System.out.println(currentEnemy.getName() + " appeared!\n");

        while (player.getHitPoints() > 0) {
            System.out.println(makePlayerMove());

            // if enemy dies
            if (currentEnemy.getHitPoints() <= 0) {
                System.out.println("You move forward through the dungeon...\n");
                if (currentEnemy.dropsLoot()) {
                    System.out.println(collectLoot(currentEnemy.getLoot()));
                }
                selectEnemy();
                System.out.println(currentEnemy.getName() + " appeared!");
            }

            System.out.println(makeEnemyMove());
        }

        System.out.println("Game over!!!");

        // starts next game
        Game nextGame = new Game();
        nextGame.run();
    }

    // MODIFIES: this
    // EFFECTS: has the current enemy attack the player and reduce its hitpoints.
    // returns string  containing information on the damage dealt.
    private String makeEnemyMove() {
        currentEnemy.hitEntity(player);
        int newHitpoints = player.getHitPoints();
        return currentEnemy.getName() + " attacks!\nYour hitpoints drop to " + newHitpoints + ".\n";
    }

    // MODIFIES: this
    // EFFECTS: allows user to view information and then eventually attack enemy by
    // taking in console input. returns string report of attack made at end of turn.
    // also has option to quit game and call System.exit()
    @SuppressWarnings("methodlength")
    private String makePlayerMove() {
        System.out.println(
                "Type the number beside an option to select it." + "\n- View Inventory [0]\n" + "- Change Weapon [1]\n"
                        + "- Change Armor [2]\n" + "- My Information [3]\n" + "- Enemy Information [4]\n"
                        + "- Drop Item [5]\n" + "- Attack Enemy [6]\n" + "- Save Game [7]\n" + "- Load Game [8]");
        String userInput = takeScannerInput();

        switch (userInput) {
            case "0":
                System.out.println(player.getInventory().toString());
                // recursive call allows player to continue with turn until attack is made
                return makePlayerMove();
            case "1":
                swapWeapon();
                return makePlayerMove();
            case "2":
                swapArmor();
                return makePlayerMove();
            case "3":
                System.out.println(player.toString() + "\n");
                return makePlayerMove();
            case "4":
                System.out.println(currentEnemy.toString() + "\n");
                return makePlayerMove();
            case "5":
                dropItem();
                return makePlayerMove();
            case "6":
                return makePlayerHit();
                // no recursive call, will always terminate with a call resulting in a string reporting attack
            case "7":
                Boolean didSave = saveGame(JSON_STORE);
                if (didSave) {
                    System.out.println("Saved game to: " + JSON_STORE);
                } else {
                    System.out.println("Unable to write to file: " + JSON_STORE);
                }
                return makePlayerMove();
            case "8":
                if (loadGame()) {
                    System.out.println("Loaded game from: " + JSON_STORE);
                } else {
                    System.out.println("Could not load game from: " + JSON_STORE);
                }
                return makePlayerMove();
            default:
                System.out.println("INVALID INPUT.\nPlease enter a number between 0 and 7.\n");
                return makePlayerMove();
        }
    }

    // MODIFIES: this
    // EFFECTS: loads player and enemy states from given file if file can be opened. returns true if file was
    // successfully read from; false otherwise
    private boolean loadGame() {
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
    public Boolean saveGame(String destination) {
        try {
            JsonWriter writer = new JsonWriter(destination);
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
        if (player.hitEntity(currentEnemy)) {
            return "You attack and slay " + currentEnemy.getName() + "!";
        } else {
            if (player.getEquippedWeapon() == null) {
                return "You punch " + currentEnemy.getName() + "! It didn't do much...";
            } else if (player.getEquippedWeapon().isOutOfUses()) {
                System.out.println("Your " + player.getEquippedWeapon().getName() + " broke!");
                player.equipWeapon(null);
            }
            return "You attack " + currentEnemy.getName() + ", reducing their health to "
                    + currentEnemy.getHitPoints() + "\n";
        }
    }

    // REQUIRES: loot is not null, loot.length > 0
    // MODIFIES: this
    // EFFECTS: attempts to pick up loot. returns string report of loot found.
    private String collectLoot(Item[] loot) {
        StringBuilder outputString = new StringBuilder();
        for (Item item : loot) {
            if (player.getInventory().pickUpItem(item)) {
                // if item is successfully picked up
                outputString.append("You found a ").append(item.getName()).append(" and add it to your inventory\n");
            } else {
                // if no space for item
                outputString.append(item.getName()).append(" was dropped but your inventory is full!\n");
            }
        }

        return outputString.toString();
    }

    // MODIFIES: this
    // EFFECTS: prompts user to equipped different weapon in inventory and equips a chosen weapon
    // if the input given is valid. if no weapons in inventory, logs no weapon message to console and
    // does not swap
    private void swapWeapon() {
        ArrayList<Weapon> weapons = player.getInventory().getWeapons();
        if (weapons.isEmpty()) {
            System.out.println("You don't have any weapons in your inventory to equip.\n");
            return;
        }

        System.out.println("Type the number beside a weapon to select it.");
        for (int i = 0; i < weapons.size(); i++) {
            System.out.println(weapons.get(i).getName() + " [" + i + "]");
        }

        String userInput = takeScannerInput();

        int selectedWeaponIndex;
        try {
            selectedWeaponIndex = Integer.valueOf(userInput);
        } catch (NumberFormatException e) {
            System.out.println("INVALID INPUT\n");
            return;
        }

        Weapon selectedWeapon = weapons.get(selectedWeaponIndex);
        player.equipWeapon(selectedWeapon);
        System.out.println("You equipped a " + selectedWeapon.getName() + ".\n");
    }

    // MODIFIES: this
    // EFFECTS: prompts user to equipped different armor in inventory and equips chosen armor
    // if the input given is valid. if no weapons in inventory, logs no weapon message to console and
    // does not swap
    private void swapArmor() {
        ArrayList<Armor> armor = player.getInventory().getArmor();
        if (armor.isEmpty()) {
            System.out.println("You don't have any armor in your inventory to equip.\n");
            return;
        }

        System.out.println("Type the number beside an armor set to select it.");
        for (int i = 0; i < armor.size(); i++) {
            System.out.println(armor.get(i).getName() + " [" + i + "]");
        }

        String userInput = takeScannerInput();

        int selectedArmorIndex;
        try {
            selectedArmorIndex = Integer.valueOf(userInput);
        } catch (NumberFormatException e) {
            System.out.println("INVALID INPUT\n");
            return;
        }

        Armor selectedArmor = armor.get(selectedArmorIndex);
        player.equipArmor(selectedArmor);
        System.out.println("You equipped a " + selectedArmor.getName() + ".\n");
    }

    // MODIFIES: this
    // EFFECTS: prompts user to selected weapon to drop. if invalid input is given to prompt,
    // produces invalid input message and does not drop anything. if valid input is given, drops
    // selected item from inventory and prints message reporting item being dropped. if no items
    // are in inventory, prints no weapons in inventory method.
    private void dropItem() {
        ArrayList<Item> items = player.getInventory().getItems();
        if (items.isEmpty()) {
            System.out.println("You don't have any items to drop!\n");
            return;
        }

        System.out.println("Type the number beside an item to drop it.");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(items.get(i).getName() + " [" + i + "]");
        }

        String userInput = takeScannerInput();

        int selectedItemIndex;
        try {
            selectedItemIndex = Integer.valueOf(userInput);
        } catch (NumberFormatException e) {
            System.out.println("INVALID INPUT\n");
            return;
        }

        Item itemToDrop = items.get(selectedItemIndex);
        player.getInventory().dropItem(itemToDrop);
        System.out.println("You dropped " + itemToDrop.getName() + ".\n");
    }
}
