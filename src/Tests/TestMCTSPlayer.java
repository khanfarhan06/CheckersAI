package Tests;

import Checkers.Game;
import Players.AlphaBetaPlayer;
import Players.Evaluator.StaticEvaluator;
import Players.MCTSPlayer;

public class TestMCTSPlayer {
    public static void main(String[] args) {
        MCTSPlayer mctsPlayer = new MCTSPlayer(10000);
        AlphaBetaPlayer alphaBetaPlayer = new AlphaBetaPlayer(4, new StaticEvaluator());
        Game game = new Game(mctsPlayer, alphaBetaPlayer);
        game.startAndShow();
    }
}
