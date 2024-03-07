package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.stream.Stream;

// Represents a parser for JSON data for a saved game
public class JsonReader {
    // path of source file
    private String source;
    // player that will be parsed from file
    private Player player = null;
    // enemy that will be parsed from file
    private Enemy enemy = null;

    // citation:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
    // EFFECTS: creates a JsonReader that reads from a given source file
    public JsonReader(String source) {
        this.source = source;
    }

    // citation:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // MODIFIES: this
    // EFFECTS: parses player and enemy from source file and sets appropriate fields equal to them.
    // throws IOException if not able to read file.
    public void generatePlayerAndEnemy() throws IOException {
        String fileData = readFile(source);
        JSONObject gameJson = new JSONObject(fileData);
        player = parsePlayerFromSave(gameJson);
        enemy = parseEnemyFromSave(gameJson);
    }

    // EFFECTS: parses player object from json representation of saved game and sets; returns parsed player
    private Player parsePlayerFromSave(JSONObject gameJson) {
        JSONObject playerJson = gameJson.getJSONObject("player");
        String name = playerJson.getString("name");
        int hitPoints = playerJson.getInt("hitPoints");
        int defence = playerJson.getInt("defence");
        int attack = playerJson.getInt("attack");
        Inventory inventory = parseInventory(playerJson.getJSONObject("inventory"));

        Player player = new Player(name, hitPoints, inventory);

        JSONObject equippedArmorJson = playerJson.getJSONObject("equippedArmor");
        player.setEquippedArmor(parseArmor(equippedArmorJson));

        if (playerJson.isNull("equippedWeapon")) {
            player.equipWeapon(null);
        } else {
            JSONObject equippedWeaponJson = playerJson.getJSONObject("equippedWeapon");
            player.equipWeapon(parseWeapon(equippedWeaponJson));
        }

        player.setDefence(defence);
        player.setAttack(attack);

        return player;
    }

    // EFFECTS: parses armor object from json representation of it
    private Armor parseArmor(JSONObject armorJson) {
        String name = armorJson.getString("name");
        int defence = armorJson.getInt("defence");
        return new Armor(name, defence);
    }

    // EFFECTS: parses weapon object from json representation of it
    private Weapon parseWeapon(JSONObject weaponJson) {
        String name = weaponJson.getString("name");
        int attack = weaponJson.getInt("attack");
        int usesRemaining = weaponJson.getInt("usesRemaining");
        return new Weapon(name, attack, usesRemaining);
    }

    // REQUIRES: item represented by itemJson must be a weapon or item
    // EFFECTS: parses weapon or armor from json representation of it.
    private Item parseItem(JSONObject itemJson) {
        if (itemJson.getString("itemType").equals("weapon")) {
            return parseWeapon(itemJson);
        } else {
            return parseArmor(itemJson);
        }
    }

    // EFFECTS: parses a list of items from json representation of it
    private ArrayList<Item> parseItems(JSONArray itemsJson) {
        ArrayList<Item> items = new ArrayList<Item>();

        for (Object itemJson : itemsJson) {
            Item parsedItem = parseItem((JSONObject) itemJson);
            items.add(parsedItem);
        }

        return items;
    }

    // EFFECTS: parses inventory from json representation of it
    private Inventory parseInventory(JSONObject inventoryJson) {
        int slots = inventoryJson.getInt("slots");
        JSONArray itemsJson = inventoryJson.getJSONArray("items");
        ArrayList<Item> items = parseItems(itemsJson);
        Inventory inventory = new Inventory(slots, items);

        return inventory;
    }

    // EFFECTS: parses enemy object from json representation of saved game
    private Enemy parseEnemyFromSave(JSONObject gameJson) {
        JSONObject enemyJson = gameJson.getJSONObject("enemy");
        String name = enemyJson.getString("name");
        int hitPoints = enemyJson.getInt("hitPoints");
        int defence = enemyJson.getInt("defence");
        int attack = enemyJson.getInt("attack");

        JSONArray lootJsonArray = enemyJson.getJSONArray("loot");
        ArrayList<Item> loot = new ArrayList<>();
        for (Object itemJson : lootJsonArray) {
            Item item = parseItem((JSONObject) itemJson);
            loot.add(item);
        }

        return new Enemy(name, hitPoints, defence, attack, loot.toArray(new Item[0]));
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Player getPlayer() {
        return player;
    }
}


