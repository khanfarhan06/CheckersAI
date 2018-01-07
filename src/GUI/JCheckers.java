package GUI;

import Checkers.CheckersBoard;

public class JCheckers {
    public static void main(String[] args) {
        CheckersBoard checkersBoard = new CheckersBoard();
        checkersBoard.setInitialBoardPosition();
        GameUserInterface gameUserInterface = new GameUserInterface(checkersBoard);
    }

    
}
