import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Test3 {
    public static void main(String[] args) {
        TextAdventureGame game = new TextAdventureGame();
        game.start();
    }
}

class TextAdventureGame {
    private AdventureModel adventureModel;
    private Scanner scanner;

    public TextAdventureGame() {
        adventureModel = new AdventureModel();
        scanner = new Scanner(System.in);
    }

    public void start() {
        adventureModel.initializeGame();
        System.out.println("Welcome to the Text Adventure Game!");
        System.out.println("Type 'start' for how to play.");

        while (true) {
            String command = scanner.nextLine();
            String output = adventureModel.executeCommand(command);
            System.out.println(output);
        }
    }
}

class AdventureModel {
    private Adventurer adventurer;
    private Map<String, Room> rooms;

    public AdventureModel() {
        adventurer = new Adventurer();
        rooms = new HashMap<>();
    }

    public void initializeGame() {
        Room startRoom = new Room("Start Room", "You are in a small, dimly lit room.");
        Room treasureRoom = new Room("Treasure Room", "You are in a room filled with glittering treasures.");
        startRoom.addExit("north", treasureRoom);
        treasureRoom.addExit("south", startRoom);
        adventurer.setCurrentRoom(startRoom);
    }

    public String executeCommand(String command) {
        String[] parts = command.toLowerCase().split(" ");
        String verb = parts[0];
        String noun = parts.length > 1 ? parts[1] : null;
        switch (verb) {
            case "start":
                return "Available commands: go [direction], look, pickup [item], drop [item], inventory, quit";
            case "go":
                if (noun != null) {
                    return adventurer.move(noun);
                } else {
                    return "Go where?";
                }
            case "look":
                return adventurer.getCurrentRoom().getDescription();
            case "pickup":
                if (noun != null) {
                    return adventurer.takeItem(noun);
                } else {
                    return "pickup what?";
                }
            case "drop":
                if (noun != null) {
                    return adventurer.dropItem(noun);
                } else {
                    return "Drop what?";
                }
            case "inventory":
                return adventurer.getInventory();
            case "quit":
                System.exit(0);
            default:
                return "I don't understand that command.";
        }
    }
}

class Adventurer {
    private Room currentRoom;
    private Map<String, Item> inventory;

    public Adventurer() {
        currentRoom = null;
        inventory = new HashMap<>();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        currentRoom = room;
    }

    public String move(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            setCurrentRoom(nextRoom);
            return "You have arrived at the " + currentRoom.getName();
        } else {
            return "You cannot go that way.";
        }
    }

    public String takeItem(String itemName) {
        Room currentRoom = getCurrentRoom();
        Item item = currentRoom.removeItem(itemName);
        if (item != null) {
            inventory.put(item.getName(), item);
            return "You picked up the " + item.getName();
        } else {
            return "There is no " + itemName + " here.";
        }
    }

    public String dropItem(String itemName) {
        Item item = inventory.remove(itemName);
        if (item != null) {
            getCurrentRoom().addItem(item);
            return "You dropped the " + itemName;
        } else {
            return "You don't have a " + itemName;
        }
    }

    public String getInventory() {
        if (inventory.isEmpty()) {
            return "Your inventory is empty.";
        } else {
            StringBuilder sb = new StringBuilder("Inventory: ");
            return sb.substring(0, sb.length());
        }
    }
}

class Room {
    private String name;
    private String description;
    private Map<String, Room> exits;
    private Map<String, Item> items;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        exits = new HashMap<>();
        items = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    public Item removeItem(String itemName) {
        return items.remove(itemName);
    }
}

class Item {
    private String name;
    private String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
