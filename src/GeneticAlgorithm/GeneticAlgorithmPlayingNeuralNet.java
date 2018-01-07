package GeneticAlgorithm;

import Checkers.Alliance;
import Checkers.Game;
import Checkers.GameResult;
import Players.RandomPlayer;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GeneticAlgorithmPlayingNeuralNet extends GeneticAlgorithm{

    public GeneticAlgorithmPlayingNeuralNet(int individualCount, int maxNumberOfGenerations, int depth, int nodeCountHiddenLayer1, int nodeCountHiddenLayer2, int matchesPerTournament) {
        super(individualCount, maxNumberOfGenerations, depth, nodeCountHiddenLayer1, nodeCountHiddenLayer2, matchesPerTournament);
    }

    public GeneticAlgorithmPlayingNeuralNet(GeneticAlgorithm geneticAlgorithm, int moreGenerations) {
        super(geneticAlgorithm, moreGenerations);
    }

    @Override
    protected void playTournament() {
        Random random  = new Random();
        ExecutorService executorService = Executors.newFixedThreadPool(2*individualCount*(2*individualCount-1));
        for(Individual individual: population){
            /*for(Individual other: population){
                if(other != individual){
                    executorService.execute(new GameRunnable(individual,other));
                }
            }*/
            int matchCounter = 0;
            while(matchCounter<matchesPerTournament){
                int randomIndividualIndex = random.nextInt(individualCount*2);
                Individual other = population.get(randomIndividualIndex);
                if(other == individual)
                    continue;
                executorService.execute(new GameRunnable(individual, other));
                matchCounter++;
            }
            /*for (int i = 0; i < matchesPerTournament; i++) {
                if(i<matchesPerTournament/2)
                    executorService.execute(new GameWithRandomPlayerRunnable(individual, Alliance.BLACK));
                else
                    executorService.execute(new GameWithRandomPlayerRunnable(individual, Alliance.WHITE));
            }*/
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class GameRunnable implements Runnable{
        Individual individual, otherIndividual;

        static int endCount = 0, startCount=0;

        public GameRunnable(Individual individual, Individual otherIndividual){
            this.individual = individual;
            this.otherIndividual = otherIndividual;
        }
        @Override
        public void run() {
            Game game = new Game(individual.getPlayer(), otherIndividual.getPlayer());
            //System.out.println("started:"+ ++startCount);
            GameResult matchResult = game.start();
            if(matchResult == GameResult.WON){
                individual.updateRecordsForMatchWon();
                otherIndividual.updateRecordsForMatchLost();
            }else if(matchResult == GameResult.LOST){
                individual.updateRecordsForMatchLost();
                otherIndividual.updateRecordsForMatchWon();
            }else if(matchResult == GameResult.DRAWN){
                individual.updateRecordsForMatchDrawn();
                otherIndividual.updateRecordsForMatchDrawn();
            }
            //System.out.println("complete: "+ ++endCount);
        }
    }

    static class GameWithRandomPlayerRunnable implements Runnable{
        private static final RandomPlayer randomPlayer= new RandomPlayer();
        Individual individual;
        Alliance alliance;
        public GameWithRandomPlayerRunnable(Individual individual, Alliance alliance){
            this.individual = individual;
            this.alliance = alliance;
        }
        @Override
        public void run() {
            if(alliance == Alliance.WHITE){
                Game game = new Game(individual.getPlayer(), randomPlayer);
                GameResult matchResult = game.start();
                if(matchResult == GameResult.WON){
                    individual.updateRecordsForMatchWon();
                }else if(matchResult == GameResult.LOST){
                    individual.updateRecordsForMatchLost();
                }else if(matchResult == GameResult.DRAWN){
                    individual.updateRecordsForMatchDrawn();
                }
            }
            else if(alliance == Alliance.BLACK){
                Game game = new Game(randomPlayer, individual.getPlayer());
                GameResult matchResult = game.start();
                if(matchResult == GameResult.WON){
                    individual.updateRecordsForMatchLost();
                }else if(matchResult == GameResult.LOST){
                    individual.updateRecordsForMatchWon();
                }else if(matchResult == GameResult.DRAWN){
                    individual.updateRecordsForMatchDrawn();
                }
            }
        }
    }
}
