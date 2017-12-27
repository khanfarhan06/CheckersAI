package Checkers;

/***
 * SimpleMove class represents a simple move in checkers game.
 * A simple move is a move in which a piece moves one place diagonally to an empty square
 * Member Variables:
 *              initialRowPosition - the row number of the square where the piece was initially present before making the move
 *              initialColumnPosition - the column number of the square where the piece was initially present before making the move
 *              finalRowPosition - the row number of the square where the piece moved to making making the move
 *              finalColumnPosition - the row number of the square where the piece moved to after making the move
 */
public class SimpleMove implements Move, Cloneable {
    int initialRowPosition;
    int initialColumnPosition;
    int finalRowPosition;
    int finalColumnPosition;

    /***
     * Default constructor
     */
    public SimpleMove(){

    }

    /***
     * Parameterized constructor
     * @param initialRowPosition The row number of the square where the piece was initially present before making the move
     * @param initialColumnPosition The column number of the square where the piece was initially present before making the move
     * @param finalRowPosition The row number of the square where the piece moved to making making the move
     * @param finalColumnPosition The row number of the square where the piece moved to after making the move
     */
    public SimpleMove(int initialRowPosition, int initialColumnPosition, int finalRowPosition, int finalColumnPosition) {
        this.initialRowPosition = initialRowPosition;
        this.finalRowPosition = finalRowPosition;
        this.initialColumnPosition = initialColumnPosition;
        this.finalColumnPosition = finalColumnPosition;
    }

    @Override
    //Returns a duplicate copy of the object which calls this method
    public SimpleMove clone(){
        //Create a new SimpleMove object
        SimpleMove copy = new SimpleMove();

        //Copy values of all the member variables of the original object into the newly created object
        copy.initialRowPosition = this.initialRowPosition;
        copy.initialColumnPosition = this.initialColumnPosition;
        copy.finalRowPosition = this.finalRowPosition;
        copy.finalColumnPosition = this.finalColumnPosition;

        //Return the cduplicate object
        return copy;
    }
}

