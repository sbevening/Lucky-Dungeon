package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArmorTest {
    private Armor genericArmor;

    @BeforeEach
    public void runBefore() {
        genericArmor = new Armor("Steel Armor", 1);
    }

    @Test
    public void constructorTest() {
        assertEquals("Steel Armor", genericArmor.getName());
        assertEquals(1, genericArmor.getDefence());
    }

    @Test
    public void toStringTest() {
        String expectedString = "Steel Armor - defence: 1";
        assertEquals(expectedString, genericArmor.toString());
    }

    @Test
    public void toJsonTest() {
        JSONObject armorJson = genericArmor.toJson();
        assertEquals("armor", armorJson.getString("itemType"));
        assertEquals("Steel Armor", armorJson.getString("name"));
        assertEquals(1, armorJson.getInt("defence"));
    }
}
