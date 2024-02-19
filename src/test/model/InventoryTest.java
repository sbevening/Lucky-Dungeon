package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Weapon weapon1;
    private Weapon weapon2;
    private Weapon weapon3;
    private Armor armor1;
    private Armor armor2;
    private Armor armor3;

    private Inventory empty;
    private Inventory halfFull;
    private Inventory almostFull;
    private Inventory completelyFull;

    @BeforeEach
    public void RunBefore() {
        weapon1 = new Weapon("Throwing Knife", 1, 1);
        weapon2 = new Weapon("Dagger", 2, 5);
        weapon3 = new Weapon("Machete", 6, 2);
        armor1 = new Armor("Chain-mail Armor", 1);
        armor2 = new Armor("Iron Armor", 2);
        armor3 = new Armor("Knight's Armor", 3);

        empty = new Inventory(3);

        halfFull = new Inventory(6);
        halfFull.pickUpItem(weapon1);
        halfFull.pickUpItem(armor2);
        halfFull.pickUpItem(weapon3);

        almostFull = new Inventory(6);
        almostFull.pickUpItem(weapon2);
        almostFull.pickUpItem(weapon2);
        almostFull.pickUpItem(weapon3);
        almostFull.pickUpItem(armor1);
        almostFull.pickUpItem(armor2);

        completelyFull = new Inventory(4);
        completelyFull.pickUpItem(weapon1);
        completelyFull.pickUpItem(armor2);
        completelyFull.pickUpItem(armor2);
        completelyFull.pickUpItem(armor3);
    }

    @Test
    public void constructorTest() {
        assertEquals(3, empty.getSlots());
        assertEquals(0, empty.getItems().size());
    }

    @Test
    public void pickUpItemTest() {
        assertTrue(empty.pickUpItem(weapon1));
        assertEquals(weapon1, empty.getItemAt(0));
        assertEquals(1, empty.getItems().size());
        assertTrue(empty.pickUpItem(armor2));
        assertEquals(armor2, empty.getItemAt(1));
        assertEquals(2, empty.getItems().size());

        assertTrue(almostFull.pickUpItem(weapon1));
        assertEquals(6, almostFull.getItems().size());
        assertEquals(weapon1, almostFull.getItemAt(5));

        assertFalse(completelyFull.pickUpItem(weapon3));
    }

    @Test
    public void dropItemTest() {
        completelyFull.dropItem(weapon1);
        assertEquals(3, completelyFull.getItems().size());
        assertEquals(armor2, completelyFull.getItemAt(0));

        completelyFull.dropItem(armor2);
        assertEquals(2, completelyFull.getItems().size());
        assertEquals(armor2, completelyFull.getItemAt(0));

        completelyFull.dropItem(armor3);
        assertEquals(1, completelyFull.getItems().size());
        assertEquals(armor2, completelyFull.getItemAt(0));

        completelyFull.dropItem(armor2);
        assertEquals(0, completelyFull.getItems().size());
    }

    @Test
    public void getItemAtTest() {
        assertEquals(weapon1, halfFull.getItemAt(0));
        assertEquals(armor2, halfFull.getItemAt(1));
        assertEquals(weapon3, halfFull.getItemAt(2));
    }

    @Test
    public void toStringTest() {
        assertEquals("Inventory is empty.", empty.toString());
        String stringWeapon1 = weapon1.toString();
        String stringArmor2 = armor2.toString();
        String stringWeapon3 = weapon3.toString();
        String expectedHalfFullString = stringWeapon1 + "\n" + stringArmor2 + "\n" + stringWeapon3 + "\n";
        assertEquals(expectedHalfFullString, halfFull.toString());
    }

    @Test
    public void getWeaponsTest() {
        assertEquals(0, empty.getWeapons().size());

        ArrayList<Weapon> halfFullWeapons = halfFull.getWeapons();
        assertEquals(2, halfFullWeapons.size());
        assertEquals(weapon1, halfFullWeapons.get(0));
        assertEquals(weapon3, halfFullWeapons.get(1));

        Inventory armorOnly = new Inventory(4);
        armorOnly.pickUpItem(armor1);
        armorOnly.pickUpItem(armor2);
        assertEquals(0, armorOnly.getWeapons().size());

        Inventory weaponsOnly = new Inventory(4);
        weaponsOnly.pickUpItem(weapon1);
        weaponsOnly.pickUpItem(weapon2);

        ArrayList<Weapon> weaponsOnlyWeapons = weaponsOnly.getWeapons();

        assertEquals(2, weaponsOnlyWeapons.size());
        assertEquals(weapon1, weaponsOnlyWeapons.get(0));
        assertEquals(weapon2, weaponsOnlyWeapons.get(1));
    }

    @Test
    public void getArmorTest() {
        assertEquals(0, empty.getArmor().size());

        ArrayList<Armor> halfFullArmor = halfFull.getArmor();
        assertEquals(1, halfFullArmor.size());
        assertEquals(armor2, halfFullArmor.get(0));

        Inventory weaponsOnly = new Inventory(4);
        weaponsOnly.pickUpItem(weapon1);
        weaponsOnly.pickUpItem(weapon2);
        assertEquals(0, weaponsOnly.getArmor().size());
    }
}