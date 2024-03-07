package ui;

import model.Player;
import model.Enemy;
import model.Weapon;
import model.Armor;
import model.Item;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

// class to represent mechanics of the game and actively output game information to console
public class Game {
    private static final String JSON_STORE = "./data/game.json";
    private Player player;
    private Enemy currentEnemy;
    private ArrayList<Enemy> enemyList;

    private int startHitpoints = 20;
    private int inventorySlots = 6;

    // EFFECTS: prompts user to start game; instantiates game and initializes starting fields in it
    public Game() {
        String titleString = "\nWELCOME TO LUCKY DUNGEON\n\nAre you ready to start exploring?\nPress enter to start!";
        System.out.println(titleString);
        takeScannerInput();
        System.out.println("You enter a really creepy dungeon... \n\n");
        player = new Player("Player", startHitpoints, inventorySlots);
        initializeEnemyList();
        selectEnemy();
    }

    private String takeScannerInput() {
        Scanner scanner = new Scanner(System.in);
        String scannerInput = scanner.nextLine();
        return scannerInput;
    }

    // MODIFIES: this
    // EFFECTS: selects random enemy from list of all enemies and sets it to
    // current enemy and also returns name of that entity
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
    // EFFECTS: initializes enemy list with all enemies in the game
    private void initializeEnemyList() {
        enemyList = new ArrayList<Enemy>();

        Weapon broadsword = new Weapon("Broadsword", 3, 7);
        Weapon dagger = new Weapon("Dagger", 1, 10);
        Weapon katana = new Weapon("Katana", 4, 8);
        Weapon stick = new Weapon("Stick", 1, 5);
        Weapon club = new Weapon("Club", 6, 5);
        Weapon dragonSlayer = new Weapon("Dragon Slayer", 10, 10);

        Armor leatherArmor = new Armor("Leather Armor", 0);
        Armor chainmailArmor = new Armor("Chainmail Armor", 1);
        Armor ironArmor = new Armor("Iron Armor", 2);

        enemyList.add(new Enemy("Baby Troll", 3, 0, 1, new Item[]{stick}));
        enemyList.add(new Enemy("Troll", 6, 0, 2, new Item[]{club}));
        enemyList.add(new Enemy("Rich Troll", 4, 0, 2, new Item[]{dagger}));
        enemyList.add(new Enemy("Ninja", 5, 0, 3, new Item[]{katana}));
        enemyList.add(new Enemy("Knight", 5, 1, 2, new Item[]{broadsword}));
        enemyList.add(new Enemy("Dragon", 5, 1, 3, new Item[]{dragonSlayer}));
        enemyList.add(new Enemy("Soldier", 4, 0, 2, new Item[]{chainmailArmor}));
        enemyList.add(new Enemy("Skeleton", 1, 0, 1, new Item[]{leatherArmor}));
        enemyList.add(new Enemy("Zombie", 4, 0, 2, new Item[]{ironArmor, dagger}));
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
                String enemyName = currentEnemy.getName();
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
        return currentEnemy.getName() + " attacks!\nYour hitpoints drop to " + Integer.toString(newHitpoints) + ".\n";
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
                JsonReader reader = new JsonReader(JSON_STORE);
                try {
                    reader.generatePlayerAndEnemy();
                    player = reader.getPlayer();
                    currentEnemy = reader.getEnemy();
                    System.out.println("Loaded save from file: " + JSON_STORE);
                } catch (IOException e) {
                    System.out.println("Unable to save to file: " + JSON_STORE);
                }
                return makePlayerMove();
            default:
                System.out.println("INVALID INPUT.\nPlease enter a number between 0 and 7.\n");
                return makePlayerMove();
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
    // EFFECTS: loads player and enemy json from file and sets them as player and enemy. returns true if game
    // is successfully loaded. else false.
    public Boolean loadGame() {
        JsonReader reader = new JsonReader(JSON_STORE);
        return true;
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
                    + Integer.toString(currentEnemy.getHitPoints()) + "\n";
        }
    }

    // REQUIRES: loot is not null, loot.length > 0
    // MODIFIES: this
    // EFFECTS: attempts to pick up loot. returns string report of loot found.
    private String collectLoot(Item[] loot) {
        String outputString = "";
        for (Item item : loot) {
            if (player.getInventory().pickUpItem(item)) {
                // if item is successfully picked up
                outputString += ("You found a " + item.getName() + " and add it to your inventory\n");
            } else {
                // if no space for item
                outputString += (item.getName() + " was dropped but your inventory is full!\n");
            }
        }

        return outputString;
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
