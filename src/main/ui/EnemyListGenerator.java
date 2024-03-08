package ui;

import model.Armor;
import model.Enemy;
import model.Item;
import model.Weapon;

import java.util.ArrayList;

// class for generating and initializing list of enemies for use in game
public class EnemyListGenerator {
    private ArrayList<Enemy> enemyList;

    // MODIFIES: this
    // EFFECTS: initializes enemy list with all enemies in the game
    public EnemyListGenerator() {
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

    public ArrayList<Enemy> getEnemyList() {
        return enemyList;
    }
}
