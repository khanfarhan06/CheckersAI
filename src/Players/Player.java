package Players;

import Checkers.Alliance;
import Checkers.CheckersBoard;
import Checkers.Move;

/***
 * Player class is the abstract class that acts as the super class of all the Players in the game
 * It has a member variable which corresponds to the alliance of the player
 * It defines the getAlliance() and setAlliance() getter setter methods for the alliance variable
 * It provides an abstract bestMove() which has to be implemented by all the subclasses of this Player class
 */
public abstract class Player {
    private Alliance alliance;

    /***
     * Getter method for alliance member variable
     * @return The alliance of this Player
     */
    public Alliance getAlliance() {
        return alliance;
    }

    /***
     * Setter method for the alliance member variable
     * @param alliance The alliance to which the alliance of this Player is to be set to
     */
    public void setAlliance(Alliance alliance){
        this.alliance = alliance;
    }

    /***
     * Returns the best move in the current board position according to this Player
     * @param checkersBoard The current board position where the best move is to be found
     * @return  A Move which is the best move in the given board position according to this Player
     */
    abstract public Move bestMove(CheckersBoard checkersBoard);

}
