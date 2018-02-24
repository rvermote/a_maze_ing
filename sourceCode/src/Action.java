import java.util.ArrayList;
import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * contains all functionality needed to run a game, given a maze and a player.
 * contains result function, which gives some generic ending messages and prints the result of a file in addition to writing it to a text file.
 * contains functionality regarding the saved instances of the maze, which are used to undo a move.
 * @author Robin Vermote
 *
 */
public class Action {

    public Action(Player player, GraphicalMaze graphics, WallMaze walls, ItemMaze items) {
        this.player = player;
        this.graphics = graphics;
        this.walls = walls;
        this.items = items;
    }

    /**
     * The player corresponding to this action instance.
     */
    private final Player player;

    public Player getPlayer() {
        return this.player;
    }

    /**
     * The graphical maze corresponding to this action instance.
     */
    private final GraphicalMaze graphics;

    public GraphicalMaze getGraphicalMaze() {
        return this.graphics;
    }

    /**
     * The wall maze corresponding to this action instance.
     */
    private final WallMaze walls;

    public WallMaze getWallMaze() {
        return this.walls;
    }

    /**
     * The item maze corresponding to this action instance.
     */
    private final ItemMaze items;

    public ItemMaze getItemMaze() {
        return this.items;
    }


    /**
     * List of the 5 latest instances of the game of this player.
     * Used when trying to undo a move.
     */
    private GameInstance[] instanceList = new GameInstance[5];

    public GameInstance[] getInstanceList() {
        return this.instanceList;
    }

    /**
     * @return the latest added index.
     */
    public GameInstance getLatestInstance() throws IllegalArgumentException {
        if (this.instanceList[0] == null)
            throw new IllegalArgumentException();
        return this.instanceList[0];
    }

    /**
     * Adds a new instance of the game at index 0, whilst moving the other instances up 1 index, thus indirectly removing the instance at index 4.
     * In this way, the 5 latest instances of the game are saved.
     *
     * @param instance The new instance to store
     */
    public void addGameInstance(GameInstance instance) {
        for (int i = getInstanceList().length - 1; i > 0; i--) {
            this.instanceList[i] = this.instanceList[i - 1];
        }
        this.instanceList[0] = instance;
    }

    /**
     * Removes the instance at index 0, whilst moving the other instances down 1 index, thus indirectly duplicating the last unique index.
     * In this way, the second latest instance can be retrieved next time the corresponding method is called.
     */
    public void removeLatestGameInstance() {
        for (int i = 0; i < getInstanceList().length - 1; i++) {
            this.instanceList[i] = this.instanceList[i + 1];
        }
    }

    /**
     * Override the current instance with a specified instance.
     *
     * @param instance The instance which will override the current instance.
     */
    public void overrideInstance(GameInstance instance) throws IllegalArgumentException {
        player.setLocation(instance.getLocation());
        player.setBag(instance.getBag());
        player.setNumberOfSteps(instance.getNumberOfSteps());
        graphics.setGraphicMaze(instance.getGraphicMaze());
        items.setItemMaze(instance.getItemMaze());
        walls.setWallMaze(instance.getWallMaze());
        removeLatestGameInstance();
    }

    /**
     * A general message displayed after a move is made.
     */
    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * List of the movement keys to be used (zqsdr for AZERTY, wasdr for QWERTY)
     */
    private String[] movementKeys = new String[5];

    public String[] getMovementKeys() {
        return this.movementKeys;
    }

    public void setMovementKeys(String[] keys) {
        this.movementKeys = keys;
    }

    /**
     * Asks for the type of keyboard and sets the movement keys according to it.
     */
    public void askKeyboard(Scanner scan) throws IllegalArgumentException {
        System.out.println("Are you using a qwerty or azerty keyboard? (type q/a)");
        String key = scan.nextLine();
        movementKeys[2] = "s";
        movementKeys[3] = "d";
        movementKeys[4] = "r";
        if (!key.equals("a") && !key.equals("q"))
            throw new IllegalArgumentException();
        if (key.equals("a")) {
            movementKeys[0] = "z";
            movementKeys[1] = "q";
        } else if (key.equals("q")) {
            movementKeys[0] = "w";
            movementKeys[1] = "a";
        }
    }

    /**
     * Asks for a move.
     */
    public static String askKey(Scanner scan) {
        System.out.println("Enter next move:");
        String key = scan.nextLine();
        return key;
    }

    /**
     * Handles the move commanded by the player.
     * 1. gets wall to check and new location of the player, according to the direction the player wants to go, or undoes move.
     * 2. Change the mazes according to the move, create a new instance of the maze, increase number of steps by 1.
     * 3. Checks for an item at the new location and manipulates it if present.
     */
    public void keyAction(String key) throws IllegalArgumentException {
        List<Integer> wallLocation = new ArrayList<Integer>();
        List<Integer> newLocation = new ArrayList<Integer>();
        setMessage("");

        if (key.equals(getMovementKeys()[0])) {
            wallLocation.add(player.getLocation().get(1) * 2);
            wallLocation.add(player.getLocation().get(0));
            newLocation.add(1);
            newLocation.add(player.getLocation().get(1) - 1);
        } else if (key.equals(getMovementKeys()[1])) {
            wallLocation.add((player.getLocation().get(1) * 2) + 1);
            wallLocation.add(player.getLocation().get(0));
            newLocation.add(0);
            newLocation.add(player.getLocation().get(0) - 1);
        } else if (key.equals(getMovementKeys()[2])) {
            wallLocation.add((player.getLocation().get(1) + 1) * 2);
            wallLocation.add(player.getLocation().get(0));
            newLocation.add(1);
            newLocation.add(player.getLocation().get(1) + 1);
        } else if (key.equals(getMovementKeys()[3])) {
            wallLocation.add((player.getLocation().get(1) * 2) + 1);
            wallLocation.add(player.getLocation().get(0) + 1);
            newLocation.add(0);
            newLocation.add(player.getLocation().get(0) + 1);
        } else if (key.equals(getMovementKeys()[4])) {
            try {
                overrideInstance(getLatestInstance());
                setMessage("Undid the previous move.");
            } catch (IllegalArgumentException e) {
                setMessage("Unable to redo last move.");
            }
        } else
            throw new IllegalArgumentException();

        if (key.equals(getMovementKeys()[0]) || key.equals(getMovementKeys()[1]) || key.equals(getMovementKeys()[2]) || key.equals(getMovementKeys()[3])) {
            switch (walls.getWallMaze().get(wallLocation.get(0)).charAt(wallLocation.get(1))) {
                case 'N':
                    addGameInstance(new GameInstance(player, graphics, items, walls));
                    graphics.changeGraphic(items.graphCoordinates(player.getLocation()), " ");
                    player.getLocation().set(newLocation.get(0), newLocation.get(1));
                    graphics.changeGraphic(items.graphCoordinates(player.getLocation()), "P");
                    player.setNumberOfSteps(player.getNumberOfSteps() + 1);
                    break;
                case 'D':
                    if (player.getBagItemIndex("K") != -1) {
                        addGameInstance(new GameInstance(player, graphics, items, walls));
                        player.setItemToUsed(player.getBagItemIndex("K"));
                        walls.changeWall(wallLocation, "N");
                        graphics.removeWallGraphic(walls.graphCoordinates(wallLocation));
                        graphics.changeGraphic(items.graphCoordinates(player.getLocation()), " ");
                        player.getLocation().set(newLocation.get(0), newLocation.get(1));
                        graphics.changeGraphic(items.graphCoordinates(player.getLocation()), "P");
                        player.setNumberOfSteps(player.getNumberOfSteps() + 1);
                    } else
                        setMessage("You need a key to open this door.");
                    break;
                case 'B':
                    if (player.getBagItemIndex("H") != -1) {
                        addGameInstance(new GameInstance(player, graphics, items, walls));
                        player.setItemToUsed(player.getBagItemIndex("H"));
                        walls.changeWall(wallLocation, "N");
                        graphics.removeWallGraphic(walls.graphCoordinates(wallLocation));
                        graphics.changeGraphic(items.graphCoordinates(player.getLocation()), " ");
                        player.getLocation().set(newLocation.get(0), newLocation.get(1));
                        graphics.changeGraphic(items.graphCoordinates(player.getLocation()), "P");
                        player.setNumberOfSteps(player.getNumberOfSteps() + 1);
                    } else
                        setMessage("You need a hammer to break this wall.");
                    break;
            }
        }

        String item = items.checkForItem(player);
        if (!item.equals("N") && !item.equals("S") && !item.equals("E") && !item.equals("T") && !item.equals("t"))
            try {
                player.addToBag(item);
            } catch (IllegalArgumentException e) {

            }
        else if (item.equals("T")) {
            graphics.changeGraphic(items.graphCoordinates(player.getLocation()), " ");
            player.setLocation(items.getStartLocation());
            graphics.changeGraphic(items.graphCoordinates(player.getLocation()), "P");
            setMessage("The force is strong with this tile. You got teleported back to the start.");
        } else if (item.equals("t")) {
            boolean hasTrophy = false;
            for (int i = 0; i < player.getBag().size(); i++) {
                if (player.getBag().get(i).equals("s") || player.getBag().get(i).equals("m") || player.getBag().get(i).equals("l")) {
                    player.getBag().set(i, "U");
                    if (hasTrophy == false)
                        hasTrophy = true;
                }
            }
            if (hasTrophy == true)
                setMessage("Oh no! A thief stole all of your trophies!");
            else
                setMessage("A thief tried to steal all of your trophies. Too bad for him, since you had none!");
        } else if (item.equals("E"))
            player.setFinished(true);
    }

    /**
     * Prints and outputs the results of the maze alongside generating some generic 'finished' messages.
     * Specifically, outputs the entered name, the maze name, the number of steps and the score.
     */
    public void result() {
        Scanner scan = new Scanner(System.in);
        System.out.println("");
        System.out.println("You completed the maze!");
        System.out.println("Enter name:");
        String name = scan.nextLine();
        int score;
        try {
            score = player.calculateScore();
        } catch (ArithmeticException e) {
            score = 0;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/robin/eclipse-workspace/A_Maze(ing)/src/scoreTable.txt", true));
            writer.newLine();
            writer.append(name + "," + graphics.getName() + "," + player.getNumberOfSteps() + "," + score);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("PLAYERNAME,MAZENAME,NUMBER_OF_STEPS,SCORE");
        System.out.println(name + "," + graphics.getName() + "," + player.getNumberOfSteps() + "," + score);
        scan.close();
    }

    /**
     * main method
     * 1. Generate an action instance alongside a graphics, walls and items maze from text file path with given name; generate a player and a scanner.
     * 2. Ask for keyboard type and display initial maze.
     * 3. Let player make a move, display maze and bag after move is made; repeat until end of maze is reached.
     * 4. Print and output results.
     */
    public static void main(String[] args) {
        GraphicalMaze graphics = new GraphicalMaze("/Users/robin/eclipse-workspace/A_Maze(ing)/src/TXTsimple.txt", "MixedMaze");
        WallMaze walls = new WallMaze("/Users/robin/eclipse-workspace/A_Maze(ing)/src/TXTsimple.txt", "MixedMaze");
        ItemMaze items = new ItemMaze("/Users/robin/eclipse-workspace/A_Maze(ing)/src/TXTsimple.txt", "MixedMaze");
        Player player = new Player(items.getStartLocation());
        Action action = new Action(player, graphics, walls, items);
        Scanner scan = new Scanner(System.in);
        try {
            action.askKeyboard(scan);
        } catch (IllegalArgumentException e) {
            System.out.println("No valid keyboard entered. Keyboard set to qwerty.");
            System.out.println("");
            action.movementKeys[0] = "w";
            action.movementKeys[1] = "a";
        }
        graphics.displayMaze();
        while (!player.isFinished()) {
            try {
                action.keyAction(askKey(scan));
            } catch (IllegalArgumentException e) {
                action.setMessage("Please enter a valid key.");
            }
            graphics.displayMaze();
            System.out.println("");
            System.out.println(player.getVisibleBag());
            System.out.println("");
            System.out.println(action.getMessage());
            System.out.println("");
        }
        action.result();
        scan.close();
    }
}
