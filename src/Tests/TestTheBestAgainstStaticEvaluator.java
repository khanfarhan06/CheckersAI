package Tests;

import Checkers.Game;
import Checkers.GameResult;
import GeneticAlgorithm.GeneticAlgorithmPlayingNeuralNet;
import GeneticAlgorithm.Individual;
import Players.AlphaBetaPlayer;
import Players.Evaluator.StaticEvaluator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestTheBestAgainstStaticEvaluator {
    public static void main(String[] args) {
        GeneticAlgorithmPlayingNeuralNet obj = TestReadFromFile.readGeneticOject();
        List<Individual> population = obj.getPopulation();
        int tournamentCount = 0;
        while(population.size()>3){
            System.out.println("Tournament no: "+ ++tournamentCount);
            playTournament(population);
            Collections.sort(population,(a,b) -> Integer.compare(b.getRoundScore(), a.getRoundScore()));
            population.stream().forEach(System.out::println);
            population.remove(population.size()-1);
        }
        saveToFile(population, "./src/Data/bestNNEvaluator.dat");
    }

    private static void saveToFile(List<Individual> population, String filepath) {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeInt(3);
            population.stream().forEach(a -> {
                try {
                    objectOutputStream.writeObject(a.getPlayer().getEvaluator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void playTournament(List<Individual> population){
        ExecutorService executorService = Executors.newFixedThreadPool(15*14);
        for(Individual individual: population){
            for(Individual other: population){
                if(individual!=other){
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Game game = new Game(individual.getPlayer(), other.getPlayer());
                            GameResult result = game.start();
                            switch (result){
                                case WON:
                                    individual.updateRecordsForMatchWon();
                                    other.updateRecordsForMatchLost();
                                    break;
                                case LOST:
                                    individual.updateRecordsForMatchLost();
                                    other.updateRecordsForMatchWon();
                                    break;
                                case DRAWN:
                                    individual.updateRecordsForMatchDrawn();
                                    other.updateRecordsForMatchDrawn();
                                    break;
                            }
                        }
                    });
                    executorService.execute(thread);
                }
            }
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
