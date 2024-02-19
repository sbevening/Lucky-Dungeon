package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {
    private Weapon singleUseWeapon;
    private Weapon multiUseWeapon;

    @BeforeEach
    public void runBefore() {
        singleUseWeapon = new Weapon("Throwing Knife", 2, 1);
        multiUseWeapon = new Weapon("Longsword", 3, 3);
    }

    @Test
    public void constructorTest() {
        assertEquals("Throwing Knife", singleUseWeapon.getName());
        assertEquals(2, singleUseWeapon.getAttack());
        assertEquals(1, singleUseWeapon.getUsesRemaining());
    }

    @Test
    public void decrementTest() {
        assertFalse(singleUseWeapon.decrementUses());
        assertEquals(0, singleUseWeapon.getUsesRemaining());

        assertTrue(multiUseWeapon.decrementUses());
        assertEquals(2, multiUseWeapon.getUsesRemaining());
        assertTrue(multiUseWeapon.decrementUses());
        assertEquals(1, multiUseWeapon.getUsesRemaining());
        assertFalse(multiUseWeapon.decrementUses());
        assertEquals(0, multiUseWeapon.getUsesRemaining());
    }

    @Test
    public void isOutOfUsesTest() {
        assertFalse(singleUseWeapon.isOutOfUses());
        singleUseWeapon.decrementUses();
        assertTrue(singleUseWeapon.isOutOfUses());
    }

    @Test
    public void toStringTest() {
        String expectedString = "Longsword - attack: 3. uses left: 3";
        assertEquals(expectedString, multiUseWeapon.toString());
    }
}