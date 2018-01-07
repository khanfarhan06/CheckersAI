package Checkers;

import java.util.ArrayList;
import java.util.List;

/***
 * CheckersBoard class represents a board position in checkers.
 * It includes the position of pieces on the board and the alliance that is to move.
 * Member Variables:
 *              board - An 8x8 array of Piece objects representing the positioning of pieces on the board
 *              allianceToMove - The Alliance that is to make the next move in the current board position
 */
public class CheckersBoard {
    private Piece[][] board = new Piece[8][8];
    private Alliance allianceToMove;
    private int plyCount = 0;

    /***
     * Default constructor
     */
    public CheckersBoard(){

    }

    @Override
    //Return a deep copy duplicate of the calling object
    public CheckersBoard clone() {
        //Create a new object of CheckersBoard class
        CheckersBoard copy = new CheckersBoard();

        //Copy the piece at each position in the current board to the duplicate board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy.board[i][j] = this.board[i][j];
            }
        }
        //Copy the allianceToMove of the current CheckersBoard object to the duplicate object
        copy.allianceToMove = this.allianceToMove;

        //Copy the plyCount
        copy.plyCount = this.plyCount;

        //Return the duplicate
        return copy;
    }

    /***
     * Getter method for the private field plyCount
     * @return  The plyCount of the current board position
     */
    public int getPlyCount() {
        return plyCount;
    }

    /***
     * Setter method for the private field allianceToMove
     * @param alliance The alliance that is to move in the current board position
     */
    public void setAllianceToMove(Alliance alliance){
        allianceToMove = alliance;
    }

    /***
     * Getter method for the private field allianceToMove
     * @return The value of the allianceToMove member variable
     */
    public Alliance getAllianceToMove() {
        return allianceToMove;
    }

    /***
     * Getter method for the private field board
     * @return The board object member variable which represents the positioning of pieces on the board
     */
    public Piece[][] getBoard(){
        return board;
    }

    /***
     * Sets up the board with the pieces positioned at places at the start of the game and WHITE to move
     */
    public void setInitialBoardPosition() {
        //Loop through all the squares on the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0) {     // Pieces are placed only on the squares whose sum of row and column number is odd
                    if (i < 3)              // For row values 0, 1 and 2 place Black Pawns
                        board[i][j] = Piece.BLACK_PAWN;
                    else if (i > 4)         // For row values 5, 6 and 7 place White Pawns
                        board[i][j] = Piece.WHITE_PAWN;
                    else                    //For other row values (3 and 4) the square is Empty
                        board[i][j] = Piece.EMPTY;
                }
            }
        }
        allianceToMove = Alliance.WHITE;    // White makes the first move, so set allianceToMove to WHITE
    }

    /***
     * Sets up the board with all the squares set to Empty
     */
    public void setAllPiecesEmpty(){
        //Loop through all the squares on the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0) {     // Pieces are placed only on the squares whose sum of row and column number is odd
                    board[i][j] = Piece.EMPTY;
                }
            }
        }
    }

    /***
     * Sets a given Piece at given row and column in the board
     * @param row Row value at which the given Piece is to be placed
     * @param column Column value at which the given Piece is to be placed
     * @param piece The Piece to be placed
     */
    public void setPieceAtPosition(int row,int column, Piece piece){
        board[row][column] = piece;
    }

    public List<Move> getAllMovesOfPieceAt(int row, int column){
        List<Move> legalMoves = new ArrayList<>();
        Piece pieceToMove = this.board[row][column];
        if(pieceToMove.getAlliance() != this.allianceToMove)
            return legalMoves;
        switch (pieceToMove){
            case WHITE_PAWN:
                legalMoves.addAll(getAllJumpMovesWhitePawn(row, column));
                if(legalMoves.isEmpty())
                    legalMoves.addAll(getAllSimpleMovesWhitePawn(row, column));
                return legalMoves;
            case BLACK_PAWN:
                legalMoves.addAll(getAllJumpMovesBlackPawn(row, column));
                if(legalMoves.isEmpty())
                    legalMoves.addAll(getAllSimpleMovesBlackPawn(row, column));
                return legalMoves;
            case WHITE_KING:
                legalMoves.addAll(getAllJumpMovesWhiteKing(row, column));
                if(legalMoves.isEmpty())
                    legalMoves.addAll(getAllSimpleMovesKing(row, column));
                return legalMoves;
            case BLACK_KING:
                legalMoves.addAll(getAllJumpMovesBlackKing(row, column));
                if(legalMoves.isEmpty())
                    legalMoves.addAll(getAllSimpleMovesKing(row, column));
                return legalMoves;
            default:
                return legalMoves;
        }
    }

    /***
     * Used to get all the possible moves in a given board position
     * @return List of Moves possible in the current board position
     */
    public List<Move> getAllMoves() {
        //Create a list of Move which holds all the possible legal moves in the current position
        List<Move> legalMoves = new ArrayList<>();

        //Add all the possible jump moves to the list of legal moves
        legalMoves.addAll(getAllJumpMoves());

        //Add all possible simple moves to the list of legal moves, only when no jump moves where present
        if (legalMoves.isEmpty())       //Simple moves are possible only when no jump move are present in the current board position
            legalMoves.addAll(getAllSimpleMoves());

        return legalMoves;
    }

    /***
     * Used to get all the possible simple moves in the given board position
     * @return List of SimpleMoves that are possible in the current board position
     */
    private List<SimpleMove> getAllSimpleMoves() {
        //Create a list of SimpleMove which will hold all the possible SimpleMove of PAWNs and KINGs in the position for the Allaince to make move
        List<SimpleMove> legalSimpleMoves = new ArrayList<>();

        //Loop through all the squares on the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0) {         // Pieces are present only on the squares whose sum of row and column number is odd
                    if (board[i][j].getAlliance() == allianceToMove) {  //Add the moves of the piece only if its Alliance is same as the Alliance that has to make the next move
                        //Call the corresponding method for the particular Piece and add the retuned list of moves to the list of legal moves
                        switch(board[i][j]){
                            case WHITE_PAWN:
                                legalSimpleMoves.addAll(getAllSimpleMovesWhitePawn(i,j));
                                break;
                            case WHITE_KING:
                                legalSimpleMoves.addAll(getAllSimpleMovesKing(i,j));
                                break;
                            case BLACK_PAWN:
                                legalSimpleMoves.addAll(getAllSimpleMovesBlackPawn(i,j));
                                break;
                            case BLACK_KING:
                                legalSimpleMoves.addAll(getAllSimpleMovesKing(i,j));
                                break;
                        }
                    }
                }
            }
        }
        return legalSimpleMoves;
    }

    /***
     * Used to get all the possible SimpleMove of a White Pawn present at given square on the board
     * @param row The row number of the White Pawn
     * @param column The column number of the White Pawn
     * @return List of SimpleMove possible for the given White Pawn in the given board position
     */
    private List<SimpleMove> getAllSimpleMovesWhitePawn(int row, int column) {
        List<SimpleMove> legalSimpleMoves = new ArrayList<>();
        if (column > 0 && this.board[row - 1][column - 1] == Piece.EMPTY)   //Condition for diagonal move in the Up Left direction
            //Create corresponding SimpleMove object for the found move and add it to list of legalSmpleMoves
            legalSimpleMoves.add(new SimpleMove(row, column, row - 1, column - 1));
        if (column < 7 && this.board[row - 1][column + 1] == Piece.EMPTY)   //Condition for diagonal move in the Up Right direction
            //Create corresponding SimpleMove object for the found move and add it to list of legalSmpleMoves
            legalSimpleMoves.add(new SimpleMove(row, column, row - 1, column + 1));
        return legalSimpleMoves;
    }

    /***
     * Used to get all the possible SimpleMove of a Black Pawn present at given square on the board
     * @param row The row number of the Black Pawn
     * @param column The column number of the Black Pawn
     * @return List of SimpleMove possible for the given Black Pawn in the given board position
     */
    private List<SimpleMove> getAllSimpleMovesBlackPawn(int row, int column) {
        List<SimpleMove> legalSimpleMoves = new ArrayList<>();
        if (column > 0 && this.board[row + 1][column - 1] == Piece.EMPTY)   //Condition for diagonal move in the Down Left direction
            //Create corresponding SimpleMove object for the found move and add it to list of legalSmpleMoves
            legalSimpleMoves.add(new SimpleMove(row, column, row + 1, column - 1));
        if (column < 7 && this.board[row + 1][column + 1] == Piece.EMPTY)   //Condition for diagonal move in the Down Right direction
            //Create corresponding SimpleMove object for the found move and add it to list of legalSmpleMoves
            legalSimpleMoves.add(new SimpleMove(row, column, row + 1, column + 1));
        return legalSimpleMoves;
    }

    /***
     * Used to get all the possible SimpleMove of a King present at given square on the board
     * Both White and Black Kings make moves in all the four directions so we dont need different methods for them whereas
     * The White pawns were restricted to move in upward direction and Black pawns to move in downward direction
     * @param row The row number of the King
     * @param column The column number of the King
     * @return List of SimpleMove possible for the given Black Pawn in the given board position
     */
    private List<SimpleMove> getAllSimpleMovesKing(int row, int column) {
        List<SimpleMove> legalSimpleMoves = new ArrayList<>();
        //Conditions to check move possibility in all 4 directions and then adding them to list of legalMoves
        if (column > 0 && row > 0 && this.board[row - 1][column - 1] == Piece.EMPTY)
            legalSimpleMoves.add(new SimpleMove(row, column, row - 1, column - 1));
        if (column < 7 && row > 0 && this.board[row - 1][column + 1] == Piece.EMPTY)
            legalSimpleMoves.add(new SimpleMove(row, column, row - 1, column + 1));
        if (column > 0 && row < 7 && this.board[row + 1][column - 1] == Piece.EMPTY)
            legalSimpleMoves.add(new SimpleMove(row, column, row + 1, column - 1));
        if (column < 7 && row < 7 && this.board[row + 1][column + 1] == Piece.EMPTY)
            legalSimpleMoves.add(new SimpleMove(row, column, row + 1, column + 1));
        return legalSimpleMoves;
    }

    /***
     * Used to get all the possible jump moves in the given board position
     * @return List of JumpMoves that are possible in the current board position
     */
    public List<JumpMove> getAllJumpMoves() {
        //Create a list of SimpleMove which will hold all the possible SimpleMove of PAWNs and KINGs in the position for the Alliance to make move
        List<JumpMove> legalMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0) {     // Pieces are present only on the squares whose sum of row and column number is odd
                    if (board[i][j].getAlliance() == allianceToMove) {     //Add the moves of the piece only if its Alliance is same as the Alliance that has to make the next move
                        //Call the corresponding method for the particular Piece and add the returned list of moves to the list of legal moves
                        switch(board[i][j]){
                            case WHITE_PAWN:
                                legalMoves.addAll(getAllJumpMovesWhitePawn(i,j));
                                break;
                            case WHITE_KING:
                                legalMoves.addAll(getAllJumpMovesWhiteKing(i,j));
                                break;
                            case BLACK_PAWN:
                                legalMoves.addAll(getAllJumpMovesBlackPawn(i,j));
                                break;
                            case BLACK_KING:
                                legalMoves.addAll(getAllJumpMovesBlackKing(i,j));
                                break;
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    /***
     * Used to get all the possible JumpMove of a White Pawn present at given square on the board
     * It calls the recursive method getAllJumpMovesWhitePawn() to add the JumpMoves to list of legal moves
     * @param row The row number of the White Pawn
     * @param column The column number of the White Pawn
     * @return List of JumpMove possible for the given White Pawn in the given board position
     */
    private List<JumpMove> getAllJumpMovesWhitePawn(int row, int column) {
        List<JumpMove> legalMoves = new ArrayList<>();
        //Call the recursive method which find all the jumpmoves for the white pawn and adds them to the List legalMoves passed to it
        getAllJumpMovesWhitePawn(legalMoves, new JumpMove(), row, column, this.clone());
        //return the updated list of legal moves
        return legalMoves;
    }

    /***
     * Used to get all the possible JumpMove of a Black Pawn present at given square on the board
     * It calls the recursive method getAllJumpMovesBlackPawn() to add the JumpMoves to list of legal moves
     * @param row The row number of the Black Pawn
     * @param column The column number of the Black Pawn
     * @return List of JumpMove possible for the given Black Pawn in the given board position
     */
    private List<JumpMove> getAllJumpMovesBlackPawn(int row, int column) {
        List<JumpMove> legalMoves = new ArrayList<>();
        //Call the recursive method which find all the jumpmoves for the black pawn and adds them to the List legalMoves passed to it.
        getAllJumpMovesBlackPawn(legalMoves, new JumpMove(), row, column, this.clone());
        //return the updated list of legal moves
        return legalMoves;
    }

    /***
     * Used to get all the possible JumpMove of a White King present at given square on the board
     * It calls the recursive method getAllJumpMovesWhiteKing() to add the JumpMoves to list of legal moves
     * @param row The row number of the White King
     * @param column The column number of the White King
     * @return List of JumpMove possible for the given White King in the given board position
     */
    private List<JumpMove> getAllJumpMovesWhiteKing(int row, int column) {
        List<JumpMove> legalMoves = new ArrayList<>();
        //Call the recursive method which find all the jumpmoves for the white king and adds them to the List legalMoves passed to it
        getAllJumpMovesWhiteKing(legalMoves, new JumpMove(), row, column, this.clone());
        //return the updated list of legal moves
        return legalMoves;
    }

    /***
     * Used to get all the possible JumpMove of a Black King present at given square on the board
     * It calls the recursive method getAllJumpMovesBlackKing() to add the JumpMoves to list of legal moves
     * @param row The row number of the Black King
     * @param column The column number of the Black King
     * @return List of JumpMove possible for the given Black King in the given board position
     */
    private List<JumpMove> getAllJumpMovesBlackKing(int row, int column) {
        List<JumpMove> legalMoves = new ArrayList<>();
        //Call the recursive method which find all the jumpmoves for the black king and adds them to the List legalMoves passed to it
        getAllJumpMovesBlackKing(legalMoves, new JumpMove(), row, column, this.clone());
        //return the updated list of legal moves
        return legalMoves;
    }

    /***
     * Recursive method which updates the list of legal moves passed to it by finding all possible jump moves for given white pawn
     * in the given board position passed to it
     * @param legalMoves The list of JumpMove found out till the present call
     * @param jumpMove The partial JumpMove fount out till the present call
     * @param row The row number of the White Pawn
     * @param column The column number of the White Pawn
     * @param checkersBoard The current board position after the partial JumpMove jumpmove has been made
     */
    private void getAllJumpMovesWhitePawn(List<JumpMove> legalMoves, JumpMove jumpMove, int row, int column, CheckersBoard checkersBoard) {
        boolean isLastJump = true;      //boolean variable to represent whether this board position was the ending of the jump move
        if (row > 1) {      // White Pawn can make a Jump Move only when the value is greater that 1
            if (column > 1 && checkersBoard.board[row - 1][column - 1].getAlliance() == Alliance.BLACK
                    && checkersBoard.board[row - 2][column - 2] == Piece.EMPTY) {   //Condition to check if Up Left Jump is possible in current board position
                //Create a copy of the partial jumpmove in order to allow the jumpmove to be used in later condition without being changed
                JumpMove jumpMoveCopy = jumpMove.clone();
                //Add the found jump to the jumpMoveCopy
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row - 2, column - 2));
                //Create a copy of the partial jumpmove in order to allow the jumpmove to be used in later condition without being changed
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                //Make the found jump on the checkersBoardCopy
                checkersBoardCopy.makeOneJump(row, column, row-2, column-2);
                //Recursive call for the new partial jump for the new board position found after making the jump and the white pawn position at the new position
                getAllJumpMovesWhitePawn(legalMoves, jumpMoveCopy, row - 2, column - 2, checkersBoardCopy);
                //Update the isLastJump boolean variable to false because as a jump is made
                isLastJump = false;
            }
            if (column < 6 && checkersBoard.board[row - 1][column + 1].getAlliance() == Alliance.BLACK
                    && checkersBoard.board[row - 2][column + 2] == Piece.EMPTY) {   //Condition to check if Up Right Jump is possible in current board position
                //TODO the cloning done here can be removed as the jumpMove and the checkersBoard is not needed by any more coming conditions
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row - 2, column + 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoard.makeOneJump(row, column, row-2, column+2);
                getAllJumpMovesWhitePawn(legalMoves, jumpMoveCopy, row - 2, column + 2, checkersBoardCopy);
            }
        }
        //Add the jumpMove found recursively to the list of legal moves, if we are at the end of the jump sequence
        //isLastJump represents whether we have reached the end sequence of the jump moves
        //The jumpMove is to be added only when it was a jump sequence not an empty jump found in places where jumpMove was not possible at all
        //  jumpMove.jumps.size()!= 0 checks the last provided condition
        if (isLastJump && jumpMove.jumps.size() != 0)
            legalMoves.add(jumpMove);
    }

    /***
     * Recursive method which updates the list of legal moves passed to it by finding all possible jump moves for given Black pawn
     * in the given board position passed to it
     * @param legalMoves The list of JumpMove found out till the present call
     * @param jumpMove The partial JumpMove fount out till the present call
     * @param row The row number of the Black Pawn
     * @param column The column number of the Black Pawn
     * @param checkersBoard The current board position after the partial JumpMove jumpmove has been made
     */
    private void getAllJumpMovesBlackPawn(List<JumpMove> legalMoves, JumpMove jumpMove, int row, int column, CheckersBoard checkersBoard) {
        //Working similar to getAllJumpMovesWhitePawn() method
        boolean isLastJump = true;
        if (row < 6) {
            if (column > 1 && checkersBoard.board[row + 1][column - 1].getAlliance() == Alliance.WHITE
                    && checkersBoard.board[row + 2][column - 2] == Piece.EMPTY) {
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row + 2, column - 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoardCopy.makeOneJump(row, column, row+2, column-2);
                getAllJumpMovesBlackPawn(legalMoves, jumpMoveCopy, row + 2, column - 2, checkersBoardCopy);
                isLastJump = false;
            }
            if (column < 6 && checkersBoard.board[row + 1][column + 1].getAlliance() == Alliance.WHITE
                    && checkersBoard.board[row + 2][column + 2] == Piece.EMPTY) {
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row + 2, column + 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoardCopy.makeOneJump(row, column, row+2, column+2);
                getAllJumpMovesBlackPawn(legalMoves, jumpMoveCopy, row + 2, column + 2, checkersBoard.clone());
                isLastJump = false;
            }
        }
        if (isLastJump && jumpMove.jumps.size() != 0)
            legalMoves.add(jumpMove);
    }

    /***
     * Recursive method which updates the list of legal moves passed to it by finding all possible jump moves for given White King
     * in the given board position passed to it
     * @param legalMoves The list of JumpMove found out till the present call
     * @param jumpMove The partial JumpMove fount out till the present call
     * @param row The row number of the White King
     * @param column The column number of the White King
     * @param checkersBoard The current board position after the partial JumpMove jumpmove has been made
     */
    private void getAllJumpMovesWhiteKing(List<JumpMove> legalMoves, JumpMove jumpMove, int row, int column, CheckersBoard checkersBoard) {
        //Working similar to getAllJumpMovesWhitePawn() method
        boolean isLastJump = true;
        if (row > 1) {
            if (column > 1 && checkersBoard.board[row - 1][column - 1].getAlliance() == Alliance.BLACK
                    && checkersBoard.board[row - 2][column - 2] == Piece.EMPTY) {
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row - 2, column - 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoardCopy.makeOneJump(row, column, row-2, column-2);
                getAllJumpMovesWhiteKing(legalMoves, jumpMoveCopy, row - 2, column - 2, checkersBoardCopy);
                isLastJump = false;
            }
            if (column < 6 && checkersBoard.board[row - 1][column + 1].getAlliance() == Alliance.BLACK
                    && checkersBoard.board[row - 2][column + 2] == Piece.EMPTY) {
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row - 2, column + 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoardCopy.makeOneJump(row, column, row-2, column+2);
                getAllJumpMovesWhiteKing(legalMoves, jumpMoveCopy, row - 2, column + 2, checkersBoardCopy);
                isLastJump = false;
            }
        }
        if (row < 6) {
            if (column > 1 && checkersBoard.board[row + 1][column - 1].getAlliance() == Alliance.BLACK
                    && checkersBoard.board[row + 2][column - 2] == Piece.EMPTY) {
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row + 2, column - 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoardCopy.makeOneJump(row, column,row+2, column-2);
                getAllJumpMovesWhiteKing(legalMoves, jumpMoveCopy, row + 2, column - 2, checkersBoardCopy);
                isLastJump = false;
            }
            if (column < 6 && checkersBoard.board[row + 1][column + 1].getAlliance() == Alliance.BLACK
                    && checkersBoard.board[row + 2][column + 2] == Piece.EMPTY) {
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row + 2, column + 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoardCopy.makeOneJump(row, column, row+2, column+2);
                getAllJumpMovesWhiteKing(legalMoves, jumpMoveCopy, row + 2, column + 2, checkersBoardCopy);
                isLastJump = false;
            }
        }
        if (isLastJump && jumpMove.jumps.size() != 0)
            legalMoves.add(jumpMove);
    }

    /***
     * Recursive method which updates the list of legal moves passed to it by finding all possible jump moves for given Black King
     * in the given board position passed to it
     * @param legalMoves The list of JumpMove found out till the present call
     * @param jumpMove The partial JumpMove fount out till the present call
     * @param row The row number of the Black King
     * @param column The column number of the Black King
     * @param checkersBoard The current board position after the partial JumpMove jumpmove has been made
     */
    private void getAllJumpMovesBlackKing(List<JumpMove> legalMoves, JumpMove jumpMove, int row, int column, CheckersBoard checkersBoard) {
        //Working similar to getAllJumpMovesWhitePawn() method
        boolean isLastJump = true;
        if (row > 1) {
            if (column > 1 && checkersBoard.board[row - 1][column - 1].getAlliance() == Alliance.WHITE
                    && checkersBoard.board[row - 2][column - 2] == Piece.EMPTY) {
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row - 2, column - 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoardCopy.makeOneJump(row, column,row-2, column-2);
                getAllJumpMovesBlackKing(legalMoves, jumpMoveCopy, row - 2, column - 2, checkersBoardCopy);
                isLastJump = false;
            }
            if (column < 6 && checkersBoard.board[row - 1][column + 1].getAlliance() == Alliance.WHITE
                    && checkersBoard.board[row - 2][column + 2] == Piece.EMPTY) {
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row - 2, column + 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoardCopy.makeOneJump(row, column, row-2, column+2);
                getAllJumpMovesBlackKing(legalMoves, jumpMoveCopy, row - 2, column + 2, checkersBoardCopy);
                isLastJump = false;
            }
        }
        if (row < 6) {
            if (column > 1 && checkersBoard.board[row + 1][column - 1].getAlliance() == Alliance.WHITE
                    && checkersBoard.board[row + 2][column - 2] == Piece.EMPTY) {
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row + 2, column - 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoardCopy.makeOneJump(row, column, row+2, column-2);
                getAllJumpMovesBlackKing(legalMoves, jumpMoveCopy, row + 2, column - 2, checkersBoardCopy);
                isLastJump = false;
            }
            if (column < 6 && checkersBoard.board[row + 1][column + 1].getAlliance() == Alliance.WHITE
                    && checkersBoard.board[row + 2][column + 2] == Piece.EMPTY) {
                JumpMove jumpMoveCopy = jumpMove.clone();
                jumpMoveCopy.jumps.add(new SimpleMove(row, column, row + 2, column + 2));
                CheckersBoard checkersBoardCopy = checkersBoard.clone();
                checkersBoardCopy.makeOneJump(row, column, row+2, column+2);
                getAllJumpMovesBlackKing(legalMoves, jumpMoveCopy, row + 2, column + 2, checkersBoard.clone());
                isLastJump = false;
            }
        }
        if (isLastJump && jumpMove.jumps.size() != 0)
            legalMoves.add(jumpMove);
    }

    /***
     * Makes a single jump on the board position that calls it with the given initial and final squares of the jump move
     * Helps the recursive getAllJumpMovesX() methods by making a single jump on the board and the Alliance to move remains same
     * @param initialRow The row position of the Piece before making the jump
     * @param initialColumn The column position of the Piece before making the jump
     * @param finalRow The row position of the Piece after making the jump
     * @param finalColumn The column position of the Piece after making the jump
     */
    private void makeOneJump(int initialRow, int initialColumn, int finalRow, int finalColumn){
        //Get the piece that whose jump is to be made
        Piece jumpingPiece = board[initialRow][initialColumn];

        //Find the row and column position of the Piece that the jumpingPiece is jumping over
        int midRow = (initialRow + finalRow)/2;
        int midColumn = (initialColumn + finalColumn)/2;

        //Make the initial square empty
        board[initialRow][initialColumn] = Piece.EMPTY;
        //Make the jumped over square empty (capture the piece)
        board[midRow][midColumn] = Piece.EMPTY;
        //Move the jumping piece to the final square
        board[finalRow][finalColumn] = jumpingPiece;
    }

    /***
     * Makes the given move on the current board position and changes the alliance to move
     * @param move The Move to be made
     */
    public void makeMove(Move move) {
        //Call the corresponding method based on whether it a JumpMove or a SimpleMove
        if (move instanceof JumpMove)   //Check if it is a JumpMove
            makeJumpMove((JumpMove) move);
        else
            makeSimpleMove((SimpleMove) move);

        //Change the alliance to move to the opposite alliance
        this.allianceToMove = allianceToMove.getOppositeAlliance();

        //Increment the plyCount by 1
        this.plyCount++;
    }

    /***
     * Makes a jump move on the current board position
     * @param jumpMove The JumpMove that has to be made
     */
    public void makeJumpMove(JumpMove jumpMove) {
        //Loop through all the SimpleMoves in the jumps list of the JumpMove and make each move on the board
        for (SimpleMove simpleMove : jumpMove.jumps) {
            //Make each single jump

            //Finding the initial square
            int initialRow = simpleMove.initialRowPosition;
            int initialColumn = simpleMove.initialColumnPosition;
            //Finding the final square
            int finalRow = simpleMove.finalRowPosition;
            int finalColumn = simpleMove.finalColumnPosition;
            //Finding the square jumped over
            int midRow = (initialRow + finalRow) / 2;
            int midColumn = (initialColumn + finalColumn) / 2;

            //Remove piece from the jumped over square
            board[midRow][midColumn] = Piece.EMPTY;
            //Move the jumping piece from initial square to the final square
            board[finalRow][finalColumn] = board[initialRow][initialColumn];
            //Remove the jumping piece from the initial square
            board[initialRow][initialColumn] = Piece.EMPTY;
        }

        //Find the final square of the piece after this jump move
        int finalColumn = jumpMove.jumps.get(jumpMove.jumps.size()-1).finalColumnPosition;
        int finalRow = jumpMove.jumps.get(jumpMove.jumps.size()-1).finalRowPosition;

        //Upgrade the pawn to a king if it is on the final row
        if(allianceToMove == Alliance.WHITE){   //For White Piece
            if(finalRow == 0 && board[finalRow][finalColumn] == Piece.WHITE_PAWN )  //Checks if the piece is on final row and is a White pawn
                board[finalRow][finalColumn] = Piece.WHITE_KING;    //Upgrade to White King
        }else {                                 //For Black Piece
            if (finalRow == 7 && board[finalRow][finalColumn] == Piece.BLACK_PAWN)  //Checks if the piece is on final row and is a Black pawn
                board[finalRow][finalColumn] = Piece.BLACK_KING;    //Upgrade to Black King
        }
    }

    /***
     * Makes a given SimpleMove on the current board position
     * @param simpleMove The SimpleMove to be made
     */
    public void makeSimpleMove(SimpleMove simpleMove) {
        //Copy the moving piece from the initial square to the final square
        board[simpleMove.finalRowPosition][simpleMove.finalColumnPosition] = board[simpleMove.initialRowPosition][simpleMove.initialColumnPosition];
        //Remove the moving piece from th initial square
        board[simpleMove.initialRowPosition][simpleMove.initialColumnPosition] = Piece.EMPTY;

        //Finding final square for the piece
        int finalRow = simpleMove.finalRowPosition;
        int finalColumn = simpleMove.finalColumnPosition;

        //Upgrade to king if the piece is a pawn and on the final row
        if(allianceToMove == Alliance.WHITE){   //For White Piece
            if(finalRow == 0 && board[finalRow][finalColumn] == Piece.WHITE_PAWN )  //Checks if the piece is on final row and is a White pawn
                board[finalRow][finalColumn] = Piece.WHITE_KING;    //Upgrade to White King
        }else {                                 //For Black Piece
            if (finalRow == 7 && board[finalRow][finalColumn] == Piece.BLACK_PAWN)  //Checks if the piece is on final row and is a Black pawn
                board[finalRow][finalColumn] = Piece.BLACK_KING;    //Upgrade to Black King
        }
    }

    /***
     * Prints out all the Pieces on their respective positions in the Board
     */
    public void showBoard() {
        System.out.println("To Move: "+allianceToMove+" Ply: "+plyCount);
        //Loop through all the squares
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0)   //Pieces are present only on the squares where sum of row and column indices is odd.
                    System.out.print(board[i][j]+" "); //Print out the piece at this square, toString() of the corresponding Piece is called
                else
                    System.out.print("- ");  //For unused squares print a hyphen
            }
            System.out.println();
        }
    }
}
