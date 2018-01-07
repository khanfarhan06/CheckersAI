package Tests;

import Players.Evaluator.NeuralNetEvaluator;
import GeneticAlgorithm.GeneticAlgorithmPlayingNeuralNet;
import GeneticAlgorithm.GeneticAlgorithmPlayingRandomPlayer;
import GeneticAlgorithm.Individual;
import Players.AlphaBetaPlayer;

public class TestGeneticAlgorithm {
    public static void main(String[] args) {
        testGeneticPlayingNN();
    }

    private static void testGeneticPlayingNN(){
        GeneticAlgorithmPlayingNeuralNet geneticAlgorithm = new GeneticAlgorithmPlayingNeuralNet(15, 250,4,40,10,5);
        geneticAlgorithm.start();
        for(Individual individual: geneticAlgorithm.getPopulation()){
            System.out.print(individual);
            AlphaBetaPlayer player = individual.getPlayer();
            NeuralNetEvaluator evaluator = (NeuralNetEvaluator) player.getEvaluator();
            System.out.println("King Value: "+evaluator.getKingValue()+"\n");
        }
        geneticAlgorithm.saveObjectToFile("./src/Data/object250genNN2LossAvgTotalLong.dat");
    }

    private static void testGeneticPlayinRandom(){
        GeneticAlgorithmPlayingRandomPlayer geneticAlgorithm = new GeneticAlgorithmPlayingRandomPlayer(15, 250,4,40,10,5);
        geneticAlgorithm.start();
        for(Individual individual: geneticAlgorithm.getPopulation()){
            System.out.println(individual);
        }
        geneticAlgorithm.saveObjectToFile("./src/Data/object250gen5match.dat");
    }
}
