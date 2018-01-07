package Tests;

import Checkers.Game;
import Players.RandomPlayer;

public class TestRandomPlayerMatch {
    public static void main(String[] args) {
        RandomPlayer player1 = new RandomPlayer();
        RandomPlayer player2 = new RandomPlayer();
        Game game = new Game(player1,player2);
        game.startAndShow();
    }
}
