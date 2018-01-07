package GeneticAlgorithm;

import Checkers.Alliance;
import Checkers.Game;
import Checkers.GameResult;
import Players.RandomPlayer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GeneticAlgorithmPlayingRandomPlayer extends GeneticAlgorithm {

    public GeneticAlgorithmPlayingRandomPlayer(int individualCount, int maxNumberOfGenerations, int depth, int nodeCountHiddenLayer1, int nodeCountHiddenLayer2, int matchesPerTournament){
        super(individualCount, maxNumberOfGenerations, depth, nodeCountHiddenLayer1, nodeCountHiddenLayer2, matchesPerTournament);
    }

    public GeneticAlgorithmPlayingRandomPlayer(GeneticAlgorithm geneticAlgorithm, int moreGenerations){
        super(geneticAlgorithm, moreGenerations);
    }

    @Override
    protected void playTournament(){
        ExecutorService executorService = Executors.newFixedThreadPool(individualCount*matchesPerTournament);
        for(Individual individual: population){
            for (int i = 0; i < matchesPerTournament/2; i++) {
                executorService.execute(new GameWithRandomPlayerRunnable(individual, Alliance.WHITE));
                executorService.execute(new GameWithRandomPlayerRunnable(individual, Alliance.BLACK));
            }
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
