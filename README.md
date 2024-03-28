# Lucky Dungeon

## Objective and Game Flow:

The primary goal of Lucky Dungeon will be to advance through a randomly
generated, unique dungeon full of different monsters and treasure by
rolling some dice! As you traverse the dungeon, you will find loads
of different **weapons**, with different levels of power and durability,
and are placed in your inventory to use at any time, and **armor** which
will reduce the damage you take when your opponent attacks you.

If at any point during your adventure that you grow tired, you can
have your adventurer take a rest and return to the dungeon once they
are rested. Traverse through the linear dungeon, do anything in your
power to protect your own life, and get as far as you can in the endless
dungeon.

## Why I Chose This Project

This game can be enjoyed by gamers who play dungeon crawler games and
turned base strategy. I personally spend a lot of my time playing
games centered around both of these features and wanted to create my own
iteration on the dungeon crawler formula. I also want to experiment with
pseudo-random generation of dungeons and how to go about creating
interesting battles that lend to replayability.

## User Stories
- As a user, I want to be able to add or remove multiple items (weapons or armor),
  X, from my inventory, Y.
- As a user, I want to be able to view a list of all the items, X, in my inventory, Y.
- As a user, I want to be able to equip different armor and weapons that can be used in battle
  to defeat enemies.
- As a user, I want to be able to battle an enemy until either myself or them runs out of hitpoints.
- As a user, I want to be able to view the different stats (defence, attack, and hitpoints) of
  myself and enemies.
- As a user, I want to be given the option to load a save game from a file while playing.
- As a user, I want to be given the option to save my game to a file while playing.

## Instructions for Grader ##

- You can generate the first required action related to the user story "adding multiple Xs to a Y" by clicking the
  "ATTACK ENEMY" button until an enemy is killed to obtain its loot (which is added to the inventory) and then selecting
  an item by clicking on it in the top panel (cannot be an empty slot) and pressing the "DROP ITEM" button to remove it
  from the inventory.
- You can generate the second required action related to the user story "adding multiple Xs to a Y" by clicking on
  an item in the inventory (cannot be an empty slot) to select it and then click the "EQUIP ITEM" button in order to
  equip a given piece of armor or a weapon and put the previously equipped item into the inventory 
- You can locate my visual component by starting the application as it is shown as a splash screen upon launch.
- You can save the state of my application by clicking on the button labelled "Save Game" while playing the game.
- You can load a saved application state by clicking on the button labelled "Load Game" while playing the game.