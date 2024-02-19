package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {
    private Enemy noLootEnemy;
    private Enemy singleLootEnemy;
    private Enemy multipleLootEnemy;
    private Item[] noLoot;
    private Item[] singleLoot;
    private Item[] multipleLoot;
    private Weapon weapon1;
    private Armor armor1;

    @BeforeEach
    public void runBefore() {
        weapon1 = new Weapon("Throwing Knife", 1, 1);
        armor1 = new Armor("Chain-mail Armor", 1);

        noLoot = new Item[] {};
        singleLoot = new Item[] {weapon1};
        multipleLoot = new Item[] {weapon1, armor1};

        noLootEnemy = new Enemy("Slime", 3, 0, 1, noLoot);
        singleLootEnemy = new Enemy("Knight", 5, 1, 1, singleLoot);
        multipleLootEnemy = new Enemy("Dragon", 15, 0, 7, multipleLoot);
    }

    @Test
    public void constructorTest() {
        assertEquals("Dragon", multipleLootEnemy.getName());
        assertEquals(15, multipleLootEnemy.getHitPoints());
        assertEquals(0, multipleLootEnemy.getDefence());
        assertEquals(7, multipleLootEnemy.getAttack());
    }

    @Test
    public void dropsLootTest() {
        assertFalse(noLootEnemy.dropsLoot());
        assertTrue(singleLootEnemy.dropsLoot());
        assertTrue(multipleLootEnemy.dropsLoot());
    }

    @Test
    public void takeHitBoundsTest() {
        assertTrue(noLootEnemy.takeHit(3));
        assertEquals(0, noLootEnemy.getHitPoints());
        assertTrue(singleLootEnemy.takeHit(7));
        assertEquals(0, singleLootEnemy.getHitPoints());
        assertFalse(multipleLootEnemy.takeHit(14));
        assertEquals(1, multipleLootEnemy.getHitPoints());
    }

    @Test
    public void takeHitWithDefenseTest() {
        assertFalse(singleLootEnemy.takeHit(1));
        assertEquals(5, singleLootEnemy.getHitPoints());
        assertFalse(singleLootEnemy.takeHit(5));
        assertEquals(1, singleLootEnemy.getHitPoints());
    }

    @Test
    public void hitEntityTest() {
        assertFalse(noLootEnemy.hitEntity(multipleLootEnemy));
        assertEquals(14, multipleLootEnemy.getHitPoints());
        assertTrue(multipleLootEnemy.hitEntity(noLootEnemy));
        assertEquals(0, noLootEnemy.getHitPoints());
        assertFalse(noLootEnemy.hitEntity(singleLootEnemy));
        assertEquals(5, singleLootEnemy.getHitPoints());
    }

    @Test
    public void getLootTest() {
        assertEquals(noLoot, noLootEnemy.getLoot());
        assertEquals(singleLoot, singleLootEnemy.getLoot());
        assertEquals(multipleLoot, multipleLootEnemy.getLoot());
    }

    @Test
    public void setDefenceTest() {
        noLootEnemy.setDefence(3);
        assertEquals(3, noLootEnemy.getDefence());
        noLootEnemy.setDefence(0);
        assertEquals(0, noLootEnemy.getDefence());
    }

    @Test
    public void toStringTest() {
        noLootEnemy = new Enemy("Slime", 3, 0, 1, noLoot);

        String nameString = "\nName: Slime";
        String hpString = "\nHitpoints: 3";
        String atkString = "\nAttack: " + 1;
        String defString = "\nDefence: " + 0;
        String titleString = "\nEnemy Stats";
        assertEquals(titleString + nameString + hpString + atkString + defString, noLootEnemy.toString());
    }
}
