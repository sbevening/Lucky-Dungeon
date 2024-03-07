package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    private Player player;
    private Enemy enemy;

    @BeforeEach
    public void runBefore() {
        Weapon stick = new Weapon("Stick", 1, 2);
        Weapon sword = new Weapon("Sword", 3, 4);
        Weapon club = new Weapon("Club", 6, 2);
        Armor chainmail = new Armor("Chainmail", 1);

        enemy = new Enemy("Goblin", 2, 4, 5, new Item[]{stick});

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(club);
        itemList.add(chainmail);
        Inventory inventory = new Inventory(4, itemList);
        player = new Player("Player", 5, inventory);
        player.equipWeapon(sword);
    }

    // citation: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/
    // JsonWriterTest.java
    @Test
    public void writerInvalidFileTest() {
        JsonWriter writer = new JsonWriter("./data/\0imagination.json");
        try {
            writer.initializeWriter();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // valid if catch block is reached
        }
    }

    @Test
    public void writerFileExistsTest() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriter.json");
            writer.initializeWriter();
            writer.write(player, enemy);

            JsonReader reader = new JsonReader("./data/testWriter.json");
            reader.generatePlayerAndEnemy();
            Player parsedPlayer = reader.getPlayer();
            Enemy parsedEnemy = reader.getEnemy();
            assertEquals("Sword", parsedPlayer.getEquippedWeapon().getName());
            assertEquals("T-Shirt", parsedPlayer.getEquippedArmor().getName());

            assertEquals(2, parsedEnemy.getHitPoints());
            assertEquals(4, parsedEnemy.getDefence());
            assertEquals(5, parsedEnemy.getAttack());

            assertTrue(itemArrayEquals(parsedEnemy.getLoot(), enemy.getLoot()));

            assertTrue(inventoriesEqual(parsedPlayer.getInventory(), player.getInventory()));
            assertTrue(playersStatsEqual(parsedPlayer, player));
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    // EFFECTS: determines if two players are equal by value
    private boolean playersStatsEqual(Player player1, Player player2) {
        Boolean equalNames = player1.getName().equals(player2.getName());
        Boolean equalAttack = player1.getAttack() == player2.getAttack();
        Boolean equalDefence = player1.getDefence() == player2.getDefence();
        return equalNames & equalDefence & equalAttack;
    }

    // REQUIRES: items of the same name must be equal by value
    // EFFECTS: determines if two inventories have equal amounts of slots and items of equal
    // names in the same slots
    private boolean inventoriesEqual(Inventory inventory1, Inventory inventory2) {
        if (inventory1.getSlots() != inventory2.getSlots()) {
            return false;
        }

        ArrayList<Item> items1 = inventory1.getItems();
        ArrayList<Item> items2 = inventory2.getItems();
        if (items1.size() != items2.size()) {
            return false;
        }

        // individually compare each item from each arraylist of equal length
        for (int i = 0; i < items1.size(); i++) {
            String name1 = items1.get(i).getName();
            String name2 = items2.get(i).getName();
            if (!(name1.equals(name2))) {
                return false;
            }
        }

        return true;
    }

    // REQUIRES: items of the same name must be equal by value
    // EFFECTS: determines if two item arrays are equal by checking if every index has an
    // item of the same name
    private Boolean itemArrayEquals(Item[] items1, Item[] items2) {
        if (items1.length != items2.length) {
            return false;
        }

        for (int i = 0; i < items1.length; i++) {
            String name1 = items1[i].getName();
            String name2 = items2[i].getName();
            if (!name1.equals(name2)) {
                return false;
            }
        }

        return true;
    }
}
