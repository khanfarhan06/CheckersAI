package Tests;

import Checkers.Game;
import Players.Evaluator.StaticEvaluator;
import GeneticAlgorithm.GeneticAlgorithmPlayingNeuralNet;
import GeneticAlgorithm.Individual;
import Players.AlphaBetaPlayer;

public class TestStrength {
    public static void main(String[] args) {
        GeneticAlgorithmPlayingNeuralNet geneticObject = TestReadFromFile.readGeneticOject();
        Individual individual = geneticObject.getPopulation().get(4);
        AlphaBetaPlayer alphaBetaPlayer = new AlphaBetaPlayer(4, new StaticEvaluator());
        Game game = new Game(alphaBetaPlayer, individual.getPlayer());
        game.startAndShow();
    }
}
