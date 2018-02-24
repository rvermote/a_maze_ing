import java.util.List;


import java.util.ArrayList;
/**
 * Handles the ability to undo moves.
 * All methods and variables can be found in other classes. This class serves to create instances of the game.
 * @author Robin Vermote
 *
 */
public class GameInstance{
    public GameInstance(Player player, GraphicalMaze graphics, ItemMaze items, WallMaze walls) {
        this.location = new ArrayList<Integer>(player.getLocation());
        this.numberOfSteps = player.getNumberOfSteps();
        this.bag = new ArrayList<String>(player.getBag());
        this.graphics = new ArrayList<String>(graphics.getGraphicMaze());
        this.items = new ArrayList<String>(items.getItemMaze());
        this.walls = new ArrayList<String>(walls.getWallMaze());
    }

    private final List<Integer> location;

    public List<Integer> getLocation(){
        return this.location;
    }

    private final int numberOfSteps;

    public int getNumberOfSteps() {
        return this.numberOfSteps;
    }

    private final List<String> bag;

    public List<String> getBag(){
        return this.bag;
    }

    private final List<String> graphics;

    public List<String> getGraphicMaze() {
        return this.graphics;
    }

    private final List<String> items;

    public List<String> getItemMaze() {
        return this.items;
    }

    private final List<String> walls;

    public List<String> getWallMaze() {
        return this.walls;
    }

}
