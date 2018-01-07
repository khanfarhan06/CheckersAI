package GUI;

import Checkers.CheckersBoard;
import Players.Player;

import javax.swing.*;
import java.util.Stack;

public class CheckersGamePanel {
    JButton[][] squares = new JButton[8][8];
    private Player player1, player2, toMove;
    private Stack<CheckersBoard> gameStack = new Stack<>();
    public CheckersGamePanel(CheckersBoard checkersBoard, Player player1, Player player2){

    }
}
