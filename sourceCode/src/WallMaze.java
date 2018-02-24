import java.util.ArrayList;
import java.util.List;
/**
 * Generates a 2D array representing the walls of the maze.
 * Contains methods to translate from wall coordinates to graphical maze coordinates and to change a wall in the wall maze.
 * @author Robin Vermote
 *
 */
public class WallMaze extends Maze{

    public WallMaze(String mazeFile, String name) {
        super(mazeFile, name);
    }

    /**
     * Array describing the walls in the maze.
     * Each element of the array represents 1 line of the maze.
     */
    private List<String> wallMaze = mazeGen();

    public List<String> getWallMaze(){
        return this.wallMaze;
    }

    public void setWallMaze(List<String> walls) {
        this.wallMaze = walls;
    }

    @Override
    public List<String> mazeGen(){
        List<String> mazeWall = new ArrayList<>();
        //for every square line (3 lines)
        for(int k=0;k<getMazeLengthY();k++) {

            int pointer =k*getMazeLengthY();
            //start of the line
            String info1 = "";
            String info2 = "";
            String info3 = "";

            //for every square in the square line
            for(int i=0+pointer;i<getMazeLengthX()+pointer;i++) {

                //first line; note: when looking at a first line somewhere in the middle of the maze
                //both the third line of the square above and the first line of this square determine the same line.
                //this doesn't lead to inconsistency however, since both should be set as the same value (the maze should have a consistent input)
                //if not, then there is no way to determine which wall should be the correct wall, so this exception should not be included.
                //in this model, except for the final square line, the northern wall of the current square will be used to determine the state of the first line.
                switch((getAbstractMaze().get(i)).get(2)) {
                    case "wall": info1+="Y"; break;
                    case "fake": info1+="N"; break;
                    case "breakable": info1+="B"; break;
                    case "door": info1+="D"; break;
                    default: info1+="N";
                }

                //second line
                switch((getAbstractMaze().get(i)).get(5)) {
                    case "wall": info2+="Y"; break;
                    case "fake": info2+="N"; break;
                    case "breakable": info2+="B"; break;
                    case "door": info2+="D"; break;
                    default: info2+="N";
                }

                //normally this should always be a wall, since this side closes the maze on the east side
                //but for consistency's sake this is still included.
                if (i == getMazeLengthX()+pointer-1) {
                    switch((getAbstractMaze().get(i)).get(4)) {
                        case "wall": info2+="Y"; break;
                        case "fake": info2+="N"; break;
                        case "breakable": info2+="B"; break;
                        case "door": info2+="D"; break;
                        default: info2+="N";
                    }
                }

                //third line in case of last row.
                if (k == getMazeLengthY()-1) {
                    switch((getAbstractMaze().get(i)).get(3)) {
                        case "wall": info3+="Y"; break;
                        case "fake": info3+="N"; break;
                        case "breakable": info3+="B"; break;
                        case "door": info3+="D"; break;
                        default: info3+="N";
                    }
                }
            }
            mazeWall.add(info1);
            mazeWall.add(info2);
            if (k == getMazeLengthY()-1) {
                mazeWall.add(info3);}
        }
        return mazeWall;}

    @Override
    public List<Integer> graphCoordinates(List<Integer> coordinates){
        List<Integer> graphicLoc = new ArrayList<Integer>();
        graphicLoc.add(coordinates.get(0));
        if (coordinates.get(0)%2 == 1)
            graphicLoc.add(coordinates.get(1)*4);
        else
            graphicLoc.add(coordinates.get(1)*4+2);
        return graphicLoc;
    }

    /**
     * Changes the wall type to the specified string input.
     *
     * @param newString
     * @param coordinates
     */
    public void changeWall(List<Integer> coordinates, String newString){
        String rowToChange = getWallMaze().get(coordinates.get(0));
        String substring1 = rowToChange.substring(0, coordinates.get(1));
        String substring2 = rowToChange.substring(coordinates.get(1)+1, getWallMaze().get(coordinates.get(0)).length());
        getWallMaze().set(coordinates.get(0), substring1 + newString + substring2);
    }

}
