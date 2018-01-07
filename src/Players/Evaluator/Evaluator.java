package Players.Evaluator;

import Checkers.CheckersBoard;
/***
 * Players.Evaluator is a functional interface which represents an evaluator function.
 * Its function is to evaluate a given board position and find the evaluated value of the board position
 * It has an abstract method evaluate() which takes a CheckersBoard object and returns the evaluated value of the board position
 * All Evaluators must implement this interface and hence give an implementation of the evaluate() and can be used to evaluate the board position
 */
public interface Evaluator {
    /***
     * Used to evaluate the given board position and find a value representing the value of the board position according to this Players.Evaluator
     * @param checkersBoard The given CheckersBoard position
     * @return The computed value of the board according to this Players.Evaluator
     */
    public abstract float evaluate(CheckersBoard checkersBoard);
}
