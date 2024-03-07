package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Player player;
    Armor weakerArmor;
    Armor strongerArmor;
    Armor defaultArmor;
    Weapon weakerWeapon;
    Weapon strongerWeapon;
    Weapon defaultWeapon;

    @BeforeEach
    public void runBefore() {
        player = new Player("Player", 5, 5);

        weakerArmor = new Armor("Leather Armor", 1);
        strongerArmor = new Armor("Iron Armor", 2);
        defaultArmor = new Armor("T-Shirt", 0);
        player.getInventory().pickUpItem(weakerArmor);
        player.getInventory().pickUpItem(strongerArmor);

        weakerWeapon = new Weapon("Sword", 2, 6);
        strongerWeapon = new Weapon("Spear", 4, 2);
        defaultWeapon = new Weapon("Dagger", 1, 4);
        player.getInventory().pickUpItem(weakerWeapon);
        player.getInventory().pickUpItem(strongerWeapon);
    }

    @Test
    public void constructorTestBlankInventory() {
        assertEquals("Player", player.getName());
        assertEquals(5, player.getHitPoints());
        assertEquals(0, player.getDefence());
        assertEquals(2, player.getAttack());
        assertEquals(5, player.getInventory().getSlots());

        Armor equippedArmor = player.getEquippedArmor();
        assertEquals("T-Shirt", equippedArmor.getName());
        assertEquals(0, equippedArmor.getDefence());

        Weapon equippedWeapon = player.getEquippedWeapon();
        assertEquals("Dagger", equippedWeapon.name);
        assertEquals(2, equippedWeapon.getAttack());
    }

    @Test
    public void constructorTestWithInventory() {
        Inventory inventory = new Inventory(4);
        inventory.pickUpItem(strongerWeapon);
        inventory.pickUpItem(weakerArmor);
        Player playerWithInventory = new Player("Player", 20, inventory);
        assertEquals("Player", playerWithInventory.getName());
        assertEquals(20, playerWithInventory.getHitPoints());
        assertEquals(0, playerWithInventory.getDefence());
        assertEquals(0, playerWithInventory.getAttack());
        assertEquals(4, playerWithInventory.getInventory().getSlots());

        assertEquals(null, playerWithInventory.getEquippedWeapon());
        assertEquals("T-Shirt", playerWithInventory.getEquippedArmor().getName());
        assertEquals(0, playerWithInventory.getEquippedArmor().getDefence());
        assertEquals(strongerWeapon, inventory.getItemAt(0));
        assertEquals(weakerArmor, inventory.getItemAt(1));
    }

    @Test
    public void setDefenceTest() {
        player.setDefence(3);
        assertEquals(3, player.getDefence());
        player.setDefence(0);
        assertEquals(0, player.getDefence());
    }

    @Test
    public void setAttackTest() {
        player.setAttack(2);
        assertEquals(2, player.getAttack());
        player.setAttack(0);
        assertEquals(0, player.getAttack());
    }


    @Test
    public void equipArmorTest() {
        player.equipArmor(weakerArmor);
        assertEquals(1, player.getDefence());
        assertEquals("Leather Armor", player.getEquippedArmor().getName());
        assertEquals(4, player.getInventory().getItems().size());

        player.equipArmor(strongerArmor);
        assertEquals(2, player.getDefence());
        assertEquals(strongerArmor, player.getEquippedArmor());
        assertEquals(4, player.getInventory().getItems().size());
        assertEquals(weakerArmor, player.getInventory().getItemAt(3));
    }

    @Test
    public void equipWeaponNotNullTest() {
        player.equipWeapon(weakerWeapon);
        assertEquals(2, player.getAttack());
        assertEquals("Sword", player.getEquippedWeapon().getName());
        assertEquals(4, player.getInventory().getItems().size());

        player.equipWeapon(strongerWeapon);
        assertEquals(4, player.getAttack());
        assertEquals(strongerWeapon, player.getEquippedWeapon());
        assertEquals(4, player.getInventory().getItems().size());
        assertEquals(weakerWeapon, player.getInventory().getItemAt(3));
    }

    @Test
    public void equipWeaponNullTest() {
        player.equipWeapon(null);
        assertEquals(null, player.getEquippedWeapon());
        assertEquals(4, player.getInventory().getItems().size());
    }

    @Test
    public void toStringTestWeaponNull() {
        String hpString = "\nHitpoints: 5";
        String weaponString = "\nNo weapon equipped";
        String armorString = "\nEquipped Armor: T-Shirt - defence: 0";
        String titleString = "\nPlayer Stats";

        player.equipWeapon(null);
        assertEquals(titleString + hpString + weaponString + armorString, player.toString());
    }

    @Test
    public void toStringTestHasWeapon() {
        String hpString = "\nHitpoints: 5";
        String weaponString = "\nEquipped Weapon: Dagger - attack: 2. uses left: 10";
        String armorString = "\nEquipped Armor: T-Shirt - defence: 0";
        String titleString = "\nPlayer Stats";
        assertEquals(titleString + hpString + weaponString + armorString, player.toString());
    }

    @Test
    public void hitEntityWeaponNotNullTest() {
        Enemy notKilled = new Enemy("Trolger", 5, 0, 1, new Item[]{});
        assertFalse(player.hitEntity(notKilled));
        assertEquals(3, notKilled.getHitPoints());

        Enemy isKilled = new Enemy("Tyler", 1, 0, 2, new Item[]{});
        assertTrue(player.hitEntity(isKilled));
        assertEquals(0, isKilled.getHitPoints());

        Enemy highDefenseNotHurt = new Enemy("Big Ogre", 3, 4, 1, new Item[]{});
        assertFalse(player.hitEntity(highDefenseNotHurt));
        assertEquals(3, highDefenseNotHurt.getHitPoints());
    }

    @Test
    public void hitEnemyWeaponNullTest() {
        player.equipWeapon(null);
        Enemy genericEnemy = new Enemy("Soldier", 3, 0, 2, new Item[]{});
        assertFalse(player.hitEntity(genericEnemy));
        assertEquals(3, genericEnemy.getHitPoints());
    }

    @Test
    public void setEquippedArmorTest() {
        player.setEquippedArmor(weakerArmor);
        assertEquals(1, player.getDefence());
        Inventory playerInventory = player.getInventory();
        assertEquals(weakerArmor, player.getEquippedArmor());
        assertEquals(weakerArmor, playerInventory.getItemAt(0));
        assertEquals(strongerArmor, playerInventory.getItemAt(1));
    }

    @Test
    public void setEquippedWeaponTest() {
        player.setEquippedWeapon(weakerWeapon);
        assertEquals(2, player.getAttack());
        Inventory playerInventory = player.getInventory();
        assertEquals(weakerWeapon, player.getEquippedWeapon());
        assertEquals(weakerArmor, playerInventory.getItemAt(0));
        assertEquals(strongerArmor, playerInventory.getItemAt(1));
    }

    @Test
    public void toJsonTestWeaponEquipped() {
        JSONObject playerJson = player.toJson();
        assertEquals("Player", playerJson.getString("name"));
        assertEquals(2, playerJson.getInt("attack"));
        assertEquals(0, playerJson.getInt("defence"));
        assertEquals(5, playerJson.getInt("hitPoints"));

        JSONObject equippedArmorJson = playerJson.getJSONObject("equippedArmor");
        assertEquals("T-Shirt", equippedArmorJson.getString("name"));
        JSONObject equippedWeaponJson = playerJson.getJSONObject("equippedWeapon");
        assertEquals("Dagger", equippedWeaponJson.getString("name"));

        JSONObject inventoryJson = playerJson.getJSONObject("inventory");
        assertEquals(5, inventoryJson.getInt("slots"));

        JSONArray itemsJson = inventoryJson.getJSONArray("items");
        JSONObject armorJson = itemsJson.getJSONObject(0);
        assertEquals("Leather Armor", armorJson.getString("name"));
        assertEquals(1, armorJson.getInt("defence"));
        JSONObject weaponJson = itemsJson.getJSONObject(2);
        assertEquals("Sword", weaponJson.getString("name"));
        assertEquals(2, weaponJson.getInt("attack"));
    }

    @Test
    public void toJsonTestNullWeaponCase() {
        player.setEquippedWeapon(null);
        JSONObject playerJson = player.toJson();
        assertTrue(playerJson.isNull("equippedWeapon"));
    }
}
