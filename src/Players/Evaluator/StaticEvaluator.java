package Players.Evaluator;

import Checkers.CheckersBoard;
import Checkers.Piece;

/***
 * StaticEvaluator class is an Players.Evaluator which finds the value of the board position based on the difference of material present on the board
 * Values of the Pieces are as follows: WHITE PAWN  +1
 *                                      BLACK PAWN  -1
 *                                      WHITE KING  +3
 *                                      BLACK KING  -3
 * The evaluated value is kept in range of -1 to +1 by dividing the found material difference value by maximum possible value i.e. 24 (for 8 Kings)
 */
public class StaticEvaluator implements Evaluator {
    @Override
    public float evaluate(CheckersBoard checkersBoard) {
        float evaluationValue = 0;      //evaluationValue stores the evaluated value of board position, it is initialized with 0
        Piece[][] board = checkersBoard.getBoard(); //Get the board postion
        //Loop through all the squares in the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0){      //Pieces are present only on square whose sum of row and column index is odd
                    if(board[i][j] == Piece.WHITE_PAWN)         //WHITE PAWN has a value +1
                        evaluationValue += 1;
                    else if(board[i][j] == Piece.BLACK_PAWN)    //BLACK PAWN has a value -1
                        evaluationValue += -1;
                    else if(board[i][j] == Piece.WHITE_KING)    //WHITE KING has a value +3
                        evaluationValue += 3;
                    else if(board[i][j] == Piece.BLACK_KING)    //BLACK KING has a value -3
                        evaluationValue += -3;
                }
            }
        }
        return  evaluationValue/36;     //Return the normalized evaluated value by dividing by 24
    }
}
