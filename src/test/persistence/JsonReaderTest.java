package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    @Test
    void readerFileDoesNotExistTest() {
        JsonReader reader = new JsonReader("./data/fakeFile.json");
        try {
            reader.generatePlayerAndEnemy();
            fail("Expected to throw IOException");
        } catch (IOException e) {
            // valid if reaches catch block
        }
    }

    @Test
    void readerEnemyTest() {
        // can pull from any file with an enemy for testing
        JsonReader reader = new JsonReader("./data/testReaderHasWeapon.json");
        try {
            reader.generatePlayerAndEnemy();
            Enemy enemy = reader.getEnemy();
            assertEquals("Dragon", enemy.getName());
            assertEquals(1, enemy.getDefence());
            assertEquals(5, enemy.getHitPoints());

            Item[] loot = enemy.getLoot();
            Weapon weapon = (Weapon) loot[0];
            assertEquals("Dragon Slayer", weapon.getName());
            assertEquals(10, weapon.getAttack());
            assertEquals(10, weapon.getUsesRemaining());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    void readerPlayerNullWeaponTest() {
        JsonReader reader = new JsonReader("./data/testReaderNullWeapon.json");
        try {
            reader.generatePlayerAndEnemy();
            Player parsedPlayer = reader.getPlayer();
            assertNull(parsedPlayer.getEquippedWeapon());
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }

    @Test
    void readerPlayerHasWeaponTest() {
        JsonReader reader = new JsonReader("./data/testReaderHasWeapon.json");
        try {
            reader.generatePlayerAndEnemy();
            Player player = reader.getPlayer();
            assertEquals("Player", player.getName());
            assertEquals(0, player.getDefence());
            assertEquals(20, player.getHitPoints());

            Weapon equippedWeapon = player.getEquippedWeapon();
            assertEquals("Dagger", equippedWeapon.getName());
            assertEquals(2, equippedWeapon.getAttack());
            assertEquals(10, equippedWeapon.getUsesRemaining());

            Armor equippedArmor = player.getEquippedArmor();
            assertEquals("T-Shirt", equippedArmor.getName());
            assertEquals(0, equippedArmor.getDefence());

            Inventory inventory = player.getInventory();
            assertEquals(6, inventory.getSlots());
            Weapon firstWeaponInInventory = (Weapon) inventory.getItemAt(0);
            assertEquals("Club", firstWeaponInInventory.getName());
            assertEquals(6, firstWeaponInInventory.getAttack());
            assertEquals(2, firstWeaponInInventory.getUsesRemaining());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }
}
