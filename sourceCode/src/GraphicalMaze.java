import java.util.ArrayList;
import java.util.List;
/**
 * Generates the maze as will be shown to the player as a 2D array; it thus holds the visual representation of the maze.
 * Alongside the generation, there are also methods to alter the visual maze:
 * 1 method removes the wall at a specified location, whilst another changes the character at a specified location.
 * @author Robin Vermote
 *
 */
public class GraphicalMaze extends Maze{

    public GraphicalMaze(String mazeFile, String name) {
        super(mazeFile, name);
    }

    /**
     * Array of the graphical representation of the maze.
     * Each element of the array represents 1 line of the graphical representation.
     */
    private List<String> graphicMaze = mazeGen();

    public List<String> getGraphicMaze(){
        return this.graphicMaze;
    }

    public void setGraphicMaze(List<String> graphics) {
        this.graphicMaze = graphics;
    }

    /**
     * Displays the maze as a graphical representation, rather than a 2D-array.
     */
    public void displayMaze() {
        for (int i=0;i<graphicMaze.size();i++) {
            System.out.println(graphicMaze.get(i));
        }
    }

    @Override
    public List<String> mazeGen(){
        List<String> mazeGraphic = new ArrayList<>();
        //for every row of tiles(3 lines)
        for(int k=0;k<getMazeLengthY();k++) {

            int pointer =k*getMazeLengthY();
            //start of the line
            String line1 = "+";
            String line2 = "";
            String line3 = "+";

            //for every tile in the tile line
            for(int i=0+pointer;i<getMazeLengthX()+pointer;i++) {

                //first line; note: when looking at a first line somewhere in the middle of the maze
                //both the third line of the square above and the first line of this square determine the same line.
                //this doesn't lead to inconsistency however, since both should be set as the same value (the maze should have a consistent input)
                //if not, then there is no way to determine which wall should be the correct wall, so this exception should not be included.
                //in this model, except for the final square line, the northern wall of the current square will be used to determine the state of the first line.
                switch((getAbstractMaze().get(i)).get(2)) {
                    case "wall": line1+="---+"; break;
                    case "fake": line1+="---+"; break;
                    case "breakable": line1+="###+"; break;
                    case "door": line1+="/d\\+"; break;
                    default: line1+="   +";
                }

                //second line
                switch((getAbstractMaze().get(i)).get(5)) {
                    case "wall": line2+="|"; break;
                    case "fake": line2+="|"; break;
                    case "breakable": line2+="#"; break;
                    case "door": line2+="\\"; break;
                    default: line2+=" ";
                }

                switch((getAbstractMaze().get(i)).get(6)){
                    case "start": line2+=" S "; break;
                    case "end": line2+=" E "; break;
                    default: line2+="   ";
                }

                //normally this should always be a wall, since this side closes the maze on the east side
                //but for consistency's sake this is still included.
                if (i == getMazeLengthX()+pointer-1) {
                    switch((getAbstractMaze().get(i)).get(4)) {
                        case "wall": line2+="|"; break;
                        case "fake": line2+="|"; break;
                        case "breakable": line2+="b"; break;
                        case "door": line2+="d"; break;
                        default: line2+="  ";
                    }
                }

                //third line in case the last row of tiles is being constructed.
                if (k == getMazeLengthY()-1) {
                    switch((getAbstractMaze().get(i)).get(3)) {
                        case "wall": line3+="---+"; break;
                        case "fake": line3+="---+"; break;
                        case "breakable": line3+="###+"; break;
                        case "door": line3+="/d\\"; break;
                        default: line3+="  +";
                    }
                }
            }
            mazeGraphic.add(line1);
            mazeGraphic.add(line2);
            if (k == getMazeLengthY()-1) {
                mazeGraphic.add(line3);}
        }
        return mazeGraphic;}

    /**
     * translates coordinates from the graphical representation to the graphical representation.
     * obsolete method but needed for consistency with the other maze types with regards to the abstract structure.
     *
     * @param coordinates
     * @return coordinates
     */
    @Override
    public List<Integer> graphCoordinates(List<Integer> coordinates){
        List<Integer> graphCoordinates = new ArrayList<Integer>();
        graphCoordinates.add(coordinates.get(0));
        graphCoordinates.add(coordinates.get(1));
        return graphCoordinates;
    }

    /**
     * Changes the graphical maze at the coordinates position.
     *
     * @param coordinates
     * 			The coordinates at which the change takes place.
     * @param character
     * 			The character to which the position is changed.
     */
    public void changeGraphic(List<Integer> coordinates, String character) {
        String frontSubString = getGraphicMaze().get(coordinates.get(1)).substring(0, (coordinates.get(0)));
        int StringLength = getGraphicMaze().get(coordinates.get(1)).length();
        String endSubString = getGraphicMaze().get(coordinates.get(1)).substring(coordinates.get(0)+1, StringLength);
        getGraphicMaze().set(coordinates.get(1), frontSubString + character + endSubString);
    }

    /**
     * Removes the wall graphic, starting from WallMaze coordinates.
     *
     * @param wallMaze
     * 			Array containing information about the walls.
     * @param coordinates
     * 			Coordinates from the WallMaze specifying which wall to remove.
     */
    public void removeWallGraphic(List<Integer> coordinates) {
        String rowOfWall = getGraphicMaze().get(coordinates.get(0));
        if (coordinates.get(0)%2 == 1) {
            String frontSubString = rowOfWall.substring(0, coordinates.get(1));
            String endSubString = rowOfWall.substring(coordinates.get(1)+1, getGraphicMaze().get(1).length());
            getGraphicMaze().set(coordinates.get(0), frontSubString + " " + endSubString);}
        else {
            String frontSubString = rowOfWall.substring(0, coordinates.get(1)-1);
            String endSubString = rowOfWall.substring(coordinates.get(1)+2, getGraphicMaze().get(1).length());
            getGraphicMaze().set(coordinates.get(0), frontSubString + "   " + endSubString);}
    }
}
