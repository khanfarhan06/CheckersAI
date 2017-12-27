package Checkers;

/***
 *  Piece Enumeration defines the types of pieces placed on the checkers board.
 *  A square on the board can have one of the four pieces or be empty.
 *  So Pieces are of five types : WHITE PAWN, BLACK PAWN, WHITE KING, BLACK KING and EMPTY.
 **/
public enum Piece {
    BLACK_PAWN{
        @Override
        public Alliance getAlliance(){
            return Alliance.BLACK;
        }

        @Override
        public String toString() {
            return "b";
        }
    },
    WHITE_PAWN{
        @Override
        public Alliance getAlliance() {
            return Alliance.WHITE;
        }

        @Override
        public String toString() {
            return "w";
        }
    },
    BLACK_KING{
        @Override
        public Alliance getAlliance(){
            return Alliance.BLACK;
        }

        @Override
        public String toString() {
            return "B";
        }
    },
    WHITE_KING{
        @Override
        public Alliance getAlliance(){
            return Alliance.WHITE;
        }

        @Override
        public String toString() {
            return "W";
        }
    },
    EMPTY{
        @Override
        public Alliance getAlliance() {
            return null;
        }

        @Override
        public String toString() {
            return "X";
        }
    };

    /***
     * Each Piece must give implementation of the getAlliance() method which returns the Alliance of that Piece
     * @return The Alliance of the Piece
     */
    public abstract Alliance getAlliance();

    //The toString() method helps in printing out the board in the showBoard() method of CheckersBoard class.
}
