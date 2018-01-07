package Players;

import Checkers.Alliance;
import Checkers.CheckersBoard;
import Checkers.Move;
import Players.Evaluator.Evaluator;

import java.io.Serializable;
import java.util.List;

/***
 * AlphaBetaPlayer represents a Player which finds the best move on the given board position by using minimax algorithm accompanied with alpha-beta pruning
 * It creates a game tree using the minimax algorithm and then evaluates the leaf board position in the game tree using an Players.Evaluator object
 * Member Variables:
 *              Players.Evaluator evaluator - It is an Players.Evaluator object which uses its evaluate() method to find the evaluated value of a given board position
 *              int depth - It is the depth upto which the AlphaBetaPlayer creates the game tree for a given board position
 */
public class AlphaBetaPlayer extends Player implements Serializable{

    private final Evaluator evaluator;
    private final int depth;

    /***
     * Parametrized constructor creates a AlphaBetaPlayer object with given evaluator and depth
     * @param depth Depth upto which the game tree is to be created
     * @param evaluator The Players.Evaluator object used to evaluate the leaf nodes of the game tree
     */
    public AlphaBetaPlayer(int depth, Evaluator evaluator) {
        this.depth = depth;
        this.evaluator = evaluator;
    }

    /***
     * Getter method for the evaluator member field
     * @return Players.Evaluator object used to evaluate a board position by this AlphaBetaPlayer
     */
    public Evaluator getEvaluator() {
        return evaluator;
    }

    /***
     * Getter method for the depth member field
     * @return The depth upto which this AlphaBetaPlayer creates the game tree
     */
    public int getDepth() {
        return depth;
    }

    @Override
    public Move bestMove(CheckersBoard checkersBoard) {
        //Get list of all legal moves in the current board position
        List<Move> legalMoves = checkersBoard.getAllMoves();
        //If no legal move was found we return null, which represents a loss of the game for the current player in the Game class
        if(legalMoves.size()==0)
            return null;

        if(legalMoves.size()==1)
            return legalMoves.get(0);

        //To find the best move in the position we need to to 1 depth of minimax search here itself
        //Otherwise the alphabeta() will return only the value of the current node not the best move
        //So we call alphabeta() for the board positions for each possible move here and find the best move by comparing there alphabeta scores
        Move bestMove = null;
        double bestScore;
        //If white is to move the best move is the move for which the resulting board position has the maximum alphabeta score because White is the maximising player
        if(checkersBoard.getAllianceToMove() == Alliance.WHITE){
            bestScore = -1.0;   //best score is set to the minimum possible score for white i.e. -1 which represents win for Black
            //Iterate through each move and call alphabeta for the resulting board position after applying that move
            for(Move move: legalMoves){
                CheckersBoard boardCopy = checkersBoard.clone();    //Create a copy of the board position for using it for the later moves
                boardCopy.makeMove(move);   //Make the move on the copy board position
                //Find the score of the current board position by calling alphabeta()
                double currentScore = alphabeta(boardCopy, depth-1, -1.0, 1.0);
                if(currentScore > bestScore){   //If this move resulted in a better score update the best score value and the best move as well
                    bestMove = move;
                    bestScore = currentScore;
                }
            }
        }
        //If Black is to move the best move is the move for which the resulting board position has the minimum alphabeta score because Black is the minimising player
        else{
            bestScore = 1.0;    //best score is set to the worst possible score for black i.e. +1 which represents win for White
            //Iterate through each move and call alphabeta for the resulting board position after applying that move
            for(Move move: legalMoves){
                CheckersBoard boardCopy = checkersBoard.clone();    //Create a copy of the board position for using it for the later moves
                boardCopy.makeMove(move);   //Make the move on the copy board position
                //Find the score of the current board position by calling alphabeta()
                double currentScore = alphabeta(boardCopy, depth-1, -1.0, 1.0);
                if(currentScore < bestScore){   //If this move resulted in a lower score update the best score value and the best move as well
                    bestMove = move;
                    bestScore = currentScore;
                }
            }
        }
        return bestMove;
    }

    /***
     * Finds the value at a given board postion by applying minimax algorithm along with alpha-beta pruning
     * and evaluates the leaf nodes of the game tree by evaluating those board position using the Players.Evaluator object
     * @param checkersBoard The given CheckersBoard object represinting the current board position to be evaluated
     * @param depth The depth upto which tree is to be searched
     * @param alpha The best value for Maximising/White player found till now
     * @param beta The best value for Minimising/Black player found till now
     * @return
     */
    private double alphabeta(CheckersBoard checkersBoard, int depth, double alpha, double beta){
        if(depth == 0)
            return evaluator.evaluate(checkersBoard);
        List<Move> legalMoves = checkersBoard.getAllMoves();
        if(legalMoves.size()==0)
            return (checkersBoard.getAllianceToMove() == Alliance.WHITE)? -1.0 : 1.0;
        /*if(legalMoves.size()==1){
            CheckersBoard boardCopy = checkersBoard.clone();
            boardCopy.makeMove(legalMoves.get(0));
            return alphabeta(boardCopy, depth+1, alpha, beta);
        }*/
        if(checkersBoard.getAllianceToMove() == Alliance.WHITE){
            double currentBest = -1.0;
            for(Move move: legalMoves){
                CheckersBoard boardCopy = checkersBoard.clone();
                boardCopy.makeMove(move);
                currentBest = Math.max(currentBest, alphabeta(boardCopy, depth-1, alpha, beta));
                if(currentBest >= beta)
                    break;
                alpha = Math.max(alpha, currentBest);
            }
            return currentBest;
        }else{
            double currentBest = 1.0;
            for(Move move: legalMoves){
                CheckersBoard boardCopy = checkersBoard.clone();
                boardCopy.makeMove(move);
                currentBest = Math.min(currentBest, alphabeta(boardCopy, depth-1, alpha, beta));
                if(currentBest <= alpha)
                    break;
                beta = Math.min(beta, currentBest);
            }
            return currentBest;
        }
    }


}
