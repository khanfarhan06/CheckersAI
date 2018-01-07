package Tests;

import Checkers.Game;
import Checkers.GameResult;
import Players.Evaluator.StaticEvaluator;
import GeneticAlgorithm.GeneticAlgorithmPlayingNeuralNet;
import GeneticAlgorithm.Individual;
import Players.AlphaBetaPlayer;
import Players.RandomPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestReadFromFile {
    static RandomPlayer randomPlayer = new RandomPlayer();
    static AlphaBetaPlayer alphaBetaPlayer= new AlphaBetaPlayer(4, new StaticEvaluator());

    public static void main(String[] args) {
        GeneticAlgorithmPlayingNeuralNet geneticObject = readGeneticOject();
        int index = 0;
        for(Individual individual: geneticObject.getPopulation()){
            System.out.print(individual);
            System.out.println("Individual: "+index++);
            playGame(individual);
        }
    }

    public static GeneticAlgorithmPlayingNeuralNet readGeneticOject(){
        try {
            FileInputStream fileInputStream = new FileInputStream("./src/Data/object250genNN2LossAvgTotalLong.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            GeneticAlgorithmPlayingNeuralNet geneticAlgorithmPlayingNeuralNet = (GeneticAlgorithmPlayingNeuralNet) objectInputStream.readObject();
            return geneticAlgorithmPlayingNeuralNet;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void playGame(Individual individual){
        individual.resetRoundScores();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Game game = new Game(individual.getPlayer(), randomPlayer);
                    GameResult matchResult = game.start();
                    if(matchResult == GameResult.WON)
                        individual.updateRecordsForMatchWon();
                    else if(matchResult == GameResult.LOST)
                        individual.updateRecordsForMatchLost();
                    else
                        individual.updateRecordsForMatchDrawn();
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("As WHITE: \n"+ individual);
        individual.resetRoundScores();
        executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Game game = new Game(randomPlayer, individual.getPlayer());
                    GameResult matchResult = game.start();
                    if(matchResult == GameResult.WON)
                        individual.updateRecordsForMatchLost();
                    else if(matchResult == GameResult.LOST)
                        individual.updateRecordsForMatchWon();
                    else
                        individual.updateRecordsForMatchDrawn();
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("As BLACK: \n"+ individual);
    }
}
