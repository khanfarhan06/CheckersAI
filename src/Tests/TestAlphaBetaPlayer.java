package Tests;

import Checkers.Game;
import Players.Evaluator.NeuralNetEvaluator;
import Players.Evaluator.StaticEvaluator;
import Players.AlphaBetaPlayer;
import Players.RandomPlayer;

public class TestAlphaBetaPlayer {
    public static void main(String[] args) {
        staticEvaluatorPlayerTest();
    }

    private static void staticEvaluatorPlayerTest(){
        AlphaBetaPlayer alphaBetaPlayer = new AlphaBetaPlayer(4,new StaticEvaluator());
        RandomPlayer randomPlayer = new RandomPlayer();
        Game game = new Game(alphaBetaPlayer, randomPlayer);
        game.startAndShow();
    }

    private static void neuralNetEvaluatorPlayerTest(){
        AlphaBetaPlayer alphaBetaPlayer = new AlphaBetaPlayer(4,new NeuralNetEvaluator(40,10));
        RandomPlayer randomPlayer = new RandomPlayer();
        Game game = new Game(alphaBetaPlayer, randomPlayer);
        game.startAndShow();
    }
}
