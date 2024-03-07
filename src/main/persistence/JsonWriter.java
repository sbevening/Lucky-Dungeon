package persistence;

import model.Enemy;
import model.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.json.JSONObject;

// Class with functionality to write JSON representations of a player and enemy to file
public class JsonWriter {
    private int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: creates new writer with given destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: initializes writer by opening destination file. throws FileNotFoundException
    // if destination cannot be opened
    public void initializeWriter() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of player and current enemy to file
    public void write(Player player, Enemy currentEnemy) {
        JSONObject playerJson = player.toJson();
        JSONObject enemyJson = currentEnemy.toJson();

        JSONObject combinedJson = new JSONObject();
        combinedJson.put("player", playerJson);
        combinedJson.put("enemy", enemyJson);

        writer.print(combinedJson.toString(TAB));
        writer.close();
    }
}
