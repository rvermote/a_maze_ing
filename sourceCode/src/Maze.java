        import java.io.FileReader;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Scanner;
        import java.util.regex.Pattern;
/**
 * abstract class which has some implemented methods regarding the generation of an 'abstract' version of the maze.
 * @author Robin Vermote
 *
 */
public abstract class Maze {

    public Maze(String mazeFile, String name) {
        this.name = name;
        try{this.abstractMaze = mazeSort(mazeRead(mazeFile));}
        catch(IOException e) {
            System.out.println("An error occured whilst trying to read the maze file.");}
        this.mazeLengthX = Integer.parseInt(getAbstractMaze().get(0).get(0))+1;
        this.mazeLengthY = getAbstractMaze().size()/getMazeLengthX();
    }

    /**
     * Name of the maze.
     */
    private final String name;

    public String getName() {
        return this.name;
    }

    /**
     * 2D array respresenting the layout of the maze.
     */
    private List<List<String>> abstractMaze;

    /**
     * returns the abstractMaze (usable from outside the class, allows information hiding of the abstractMaze variable)
     *
     * @return the abstractMaze
     */
    public List<List<String>> getAbstractMaze() {
        return this.abstractMaze;
    }

    /**
     * The length of the maze along the X axis.
     */
    private int mazeLengthX;

    public int getMazeLengthX() {
        return this.mazeLengthX;
    }

    /**
     * the length of the maze along the Y axis.
     */
    private int mazeLengthY;

    public int getMazeLengthY() {
        return this.mazeLengthY;
    }

    /**
     * Translates the input text file describing the maze to a 2D array.
     *
     * @param mazeFile (the input file describing the layout of the maze)
     * @return A 2D array representation of the input file.
     */
    public List<List<String>> mazeRead(String mazeFile) throws IOException{

        List<List<String>> overview = new ArrayList<List<String>>();
        List<String> header = new ArrayList<>();
        int variables = 7;

        try(Scanner scan = new Scanner(new FileReader(mazeFile))) {
            scan.useDelimiter(Pattern.compile("(,)|(\r\n)"));
            for(int i=0; i<variables;i++) {
                header.add(scan.next());
            }
            while(scan.hasNext()) {
                List<String> row = new ArrayList<>();
                for(int i=0; i<variables; i++) {
                    row.add(scan.next());
                }
                overview.add(row);
            }
        }
        catch(IOException e) {e.printStackTrace();}
        return overview;
    }

    /**
     * extra: Resorting the 2D array to prepare for maze creation.
     * this is already done by default in the 2 provided examples, but this method generalizes this to maze files where the coordinates aren't ordered.
     * the first element will have [0,0] coordinates, the last will have [n,m] coordinates, with n and m the outer x and y limits of the maze.
     * in between, elements are ordered by keeping the Ycoordinate constant until all Xcoordinates for a certain Y are passed.
     * this way, the maze can be easily horizontally rendered (later step).
     *
     * @param overview (the 2D array representation of the maze)
     * @return overview (the same 2D array representation, but resorted)
     */
    public List<List<String>> mazeSort(List<List<String>> overview) throws IllegalArgumentException{

        //order x coordinates
        for(int i=1;i<overview.size();i++) {
            for(int j=0;j<i;j++) {
                if( Integer.parseInt((overview.get(i)).get(0)) > Integer.parseInt((overview.get(j)).get(0)) ) {
                    overview.add(j,overview.get(i));
                    overview.remove(i+1);
                }
            }
        }

        int XLength = Integer.parseInt((overview.get(0)).get(0))+1;
        int YLength = overview.size()/XLength;

        //split up x coordinates
        for(int i=0;i<(overview.size())/XLength;i++) {
            int pointer = i*YLength;
            //for all y coordinates corresponding to the same x coordinate
            for(int j=1+pointer ; j<YLength+pointer; j++) {
                //check element at position j against element at position k, for all elements k lower than j
                for(int k=0+pointer;k<j;k++) {
                    if( Integer.parseInt((overview.get(j)).get(1)) <  Integer.parseInt((overview.get(k)).get(1)))  {
                        overview.add(k,overview.get(j));
                        overview.remove(j+1);
                    }
                }
            }
        }
        System.out.println(overview);
        return overview;
    }

    /**
     * Generates the type of maze in an array.
     * @return
     * 		An array representing the maze.
     */
    public abstract List<String> mazeGen();

    /**
     * Translates coordinates of the type of maze to coordinates of the graphical maze.
     * @param coordinates
     * 			The coordinates to be translated
     * @return
     * 			The translated coordinates
     */
    public abstract List<Integer> graphCoordinates(List<Integer> coordinates);

}