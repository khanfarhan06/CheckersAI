package Players;

import Checkers.CheckersBoard;
import Checkers.JumpMove;
import Checkers.Move;
import Checkers.SimpleMove;

import java.util.Scanner;

public class HumanPlayer extends Player {
    Scanner scanner = new Scanner(System.in);
    @Override
    public Move bestMove(CheckersBoard checkersBoard) {
        System.out.println("Enter your move: ");
        String inputMove = scanner.nextLine();
        String[] squareStrings = inputMove.split(" ");
        int[][] squares = new int[squareStrings.length][2];
        for (int i = 0; i < squareStrings.length; i++) {
            int square = Integer.parseInt(squareStrings[i]);
            squares[i][0] = square/10 - 1;
            squares[i][1] = square%10 - 1;
        }

        if (squares.length == 2){
            if(Math.abs(squares[1][0]-squares[0][0])==1){
                return new SimpleMove(squares[0][0], squares[0][1], squares[1][0], squares[1][1]);
            }
        }
        JumpMove move = new JumpMove();
        for (int i = 0; i < squares.length-1; i++) {
            move.jumps.add(new SimpleMove(squares[i][0], squares[i][1], squares[i+1][0], squares[i+1][1]));
        }
        return move;
    }
}
