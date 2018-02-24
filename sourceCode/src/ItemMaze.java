import java.util.ArrayList;
import java.util.List;


/**
 * Holds the method to produce a 2D array representing the items located at each position of the maze.
 * Contains methods to translate from item coordinates to graphical maze coordinates, retrieve the start location of the player
 * and check for an item at given coordinates.
 * @author Robin Vermote
 *
 */
public class ItemMaze extends Maze{

    public ItemMaze(String mazeFile, String name) {
        super(mazeFile, name);
    }

    /**
     * Array describing the items located at every position.
     * Each element of the array represents 1 line of the maze.
     */
    private List<String> ItemMaze = mazeGen();

    public List<String> getItemMaze(){
        return this.ItemMaze;
    }

    public void setItemMaze(List<String> items){
        this.ItemMaze = items;
    }

    @Override
    public List<String> mazeGen(){
        List<String> mazeItemInfo = new ArrayList<>();
        //for every row of tiles
        for(int k=0;k<getMazeLengthY();k++) {

            int pointer =k*getMazeLengthY();
            //start of the line
            String infoItem = "";

            //for every tile in the tile line
            for(int i=0+pointer;i<getMazeLengthX()+pointer;i++) {
                switch((getAbstractMaze().get(i)).get(6)){
                    case "start": infoItem+="S"; break;
                    case "end": infoItem+="E"; break;
                    case "key": infoItem+="K"; break;
                    case "hammer": infoItem+="H"; break;
                    case "sTrophy": infoItem+="s"; break;
                    case "mTrophy": infoItem+="m"; break;
                    case "lTrophy": infoItem+="l"; break;
                    case "teleport": infoItem+="T"; break;
                    case "thief": infoItem+="t"; break;
                    default: infoItem+="N";
                }}
            mazeItemInfo.add(infoItem);}
        return mazeItemInfo;}

    @Override
    public List<Integer> graphCoordinates(List<Integer> coordinates){
        List<Integer> graphicLoc = new ArrayList<Integer>();
        graphicLoc.add(2+(coordinates.get(0)*4));
        graphicLoc.add(1+(coordinates.get(1)*2));
        return graphicLoc;
    }

    /**
     * returns the ItemMaze starting location of the player in the maze.
     */
    public List<Integer> getStartLocation(){
        List<Integer> location = new ArrayList<Integer>();
        for(int i=0; i<getMazeLengthY(); i++) {
            for(int j=0; j<getMazeLengthX(); j++) {
                if (this.getItemMaze().get(i).charAt(j) == "S".codePointAt(0)) {
                    location.add(j);
                    location.add(i);
                    return location;
                }
            }
        }
        return location;
    }

    /**
     * Checks if an add-able item is present at the player's location, return that item and remove the item from the item maze.
     */
    public String checkForItem(Player player) {
        String item = "N";
        int yLocation = player.getLocation().get(1);
        int xLocation = player.getLocation().get(0);
        if (getItemMaze().get(yLocation).charAt(xLocation) != "N".codePointAt(0) && getItemMaze().get(yLocation).charAt(xLocation) != "S".codePointAt(0)) {
            item = getItemMaze().get(yLocation).substring(xLocation, xLocation+1);
            String substr1 = getItemMaze().get(yLocation).substring(0, xLocation);
            String substr2 = getItemMaze().get(yLocation).substring(xLocation+1, getItemMaze().get(yLocation).length());
            getItemMaze().set(yLocation, substr1 + "N" + substr2);
        }
        return item;}
}
