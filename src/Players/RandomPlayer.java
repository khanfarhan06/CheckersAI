package Players;

import Checkers.CheckersBoard;
import Checkers.Move;

import java.util.List;
import java.util.Random;

/***
 * RandomPlayer class represents a Player which makes a Random move in the list of possible moves in the current board position
 * It is a subclass of the Payer abstract class, so it provides an implementation of the bestMove() method
 */
public class RandomPlayer extends Player {

    private final Random randomGenerator = new Random();    //A random generator object

    @Override
    public Move bestMove(CheckersBoard checkersBoard) {
        //get all possible moves in the current board position
        List<Move> legalMoves = checkersBoard.getAllMoves();
        //If no possible move was found return a null value,thsi is used in Game class's start() to find if the Player has no possible moves
        if (legalMoves.size()==0)   //Condition to check if no possible moves where present
            return null;
        //Generate a random index from 0 to the size of the list of the legal moves
        int index = randomGenerator.nextInt(legalMoves.size());

        //Return the move at the generated random index
        return legalMoves.get(index);
    }
}
