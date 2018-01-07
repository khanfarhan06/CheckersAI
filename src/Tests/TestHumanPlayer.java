package Tests;

import Checkers.Game;
import Players.HumanPlayer;
import Players.RandomPlayer;

public class TestHumanPlayer {
    public static void main(String[] args) {
        HumanPlayer player1 = new HumanPlayer();
        RandomPlayer player2 = new RandomPlayer();
        Game game = new Game(player1, player2);
        game.startAndShow();
    }
}
