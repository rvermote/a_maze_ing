package player;

import java.util.ArrayList;
import java.util.List;
/**
 * Contains all functionality a player needs to be able to play the game.
 * @author Robin Vermote
 *
 */
public class Player {
    public Player(List<Integer> location) {
        setLocation(location);
    }

    /**
     * The location the player is currently at in ItemMaze coordinates.
     */
    private List<Integer> location = new ArrayList<Integer>();

    public List<Integer> getLocation(){
        return this.location;
    }

    public void setLocation(List<Integer> location) {
        this.location = location;
    }

    /**
     * The number of steps currently taken by the player.
     */
    private int numberOfSteps = 0;

    public int getNumberOfSteps() {
        return this.numberOfSteps;
    }

    public void setNumberOfSteps(int newNumberOfSteps) {
        this.numberOfSteps = newNumberOfSteps;
    }

    /**
     * List containing the state of the items picked up by the player.
     */
    private List<String> bag = new ArrayList<String>();

    public List<String> getBag(){
        return this.bag;
    }

    public void setBag(List<String> bag) {
        this.bag = bag;
    }

    /**
     * Translates the abstract bag into a human-readable bag.
     * Detailed: doesn't show used items, gives every 1-letter coded item a readable name.
     * @return the translated bag
     */

    public List<String> getVisibleBag(){
        List<String> bag = getBag();
        List<String> translatedBag = new ArrayList<String>();
        for(int i=0;i<bag.size();i++) {
            switch(bag.get(i)) {
                case "K": translatedBag.add("key"); break;
                case "H": translatedBag.add("hammer"); break;
                case "s": translatedBag.add("small trophy"); break;
                case "m": translatedBag.add("medium trophy"); break;
                case "l": translatedBag.add("large trophy"); break;
            }
        }
        return translatedBag;
    }

    /**
     * Adds an item to the bag.
     *
     * @param item
     * 			The item to be added.
     */
    public void addToBag(String item){
        this.bag.add(item);
    }

    /**
     * Returns the highest index of a kind of item found in the bag.
     * e.g. 2 keys at index 2 and 5, will return index 5.
     *
     * @param item
     * 			The kind of item.
     *
     * @return The highest index of the kind of item.
     */
    public int getBagItemIndex(String item) {
        int itemIndex = -1;
        for (int i=0;i<bag.size(); i++) {
            if (bag.get(i).equals(item))
                itemIndex = i;
        }
        return itemIndex;
    }

    /**
     * Sets item at the index to used.
     * @param index
     */
    public void setItemToUsed(int index) {
        this.bag.set(index, "U");
    }

    /**
     * Specifies whether the player has finished the maze or not.
     */
    private boolean finished = false;

    public boolean isFinished() {
        return this.finished;
    }

    public void setFinished(boolean state) {
        this.finished = state;
    }

    /**
     * Calculates the score based on the possessed trophies and the number of steps taken to complete the maze.
     */
    public int calculateScore() throws ArithmeticException{
        int score = 0;
        for(int i=0;i<getBag().size();i++) {
            switch(getBag().get(i)) {
                case "s": score += 25; break;
                case "m": score += 50; break;
                case "l": score += 100; break;
            }
        }
        if(score - getNumberOfSteps() < 0)
            throw new ArithmeticException();
        return score - getNumberOfSteps();
    }
}
