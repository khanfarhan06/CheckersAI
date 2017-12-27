package Checkers;

import java.util.ArrayList;
import java.util.List;

/**
 * JumpMove class represents a jump move in the game of checkers
 * A jump move can be a single jump or multiple jumps, these individual jumps are stored as an object of SimpleMove class
 * In a single jump the piece moves two places diagonally from its original position and the opposition piece that it jumps over is captured
 * Member Variables:
 *              jumps : List of all the individual jumps which make up this jump move,
 *                      the individual jumps are represented as individual SimpleMove objects
 */
public class JumpMove implements Move, Cloneable{
    public List<SimpleMove> jumps = new ArrayList<>();

    @Override
    //Returns a deep copied duplicate of the object tha calls this method
    public JumpMove clone(){
        //Create a new object of JumpMove
        JumpMove copy = new JumpMove();

        //For each SimpleMove in the calling JumpMoves's jumps, create a copy of the SimpleMove and add it to the newly created objects jumps list
        for(SimpleMove move: this.jumps){
            copy.jumps.add(move.clone());
        }

        //Return the deep copied duplicate object
        return copy;
    }
}
