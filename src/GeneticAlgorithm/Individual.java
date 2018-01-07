package GeneticAlgorithm;

import Players.Evaluator.NeuralNetEvaluator;
import Players.AlphaBetaPlayer;

import java.io.Serializable;

public class Individual implements Serializable{
    private AlphaBetaPlayer player;
    private int roundMatchesPlayed, roundMatchesWon, roundMatchesLost, roundMatchesDrawn;
    private int totalMatchesPlayed, totalMatchesWon, totalMatchesLost, totalMatchesDrawn;

    public Individual(int depth, int nodeCountHiddenLayer1,int nodeCountHiddenlayer2){
        player = new AlphaBetaPlayer(depth, new NeuralNetEvaluator(nodeCountHiddenLayer1, nodeCountHiddenlayer2));
    }

    public Individual(Individual individual, boolean isMutated){
        player = new AlphaBetaPlayer(individual.player.getDepth(), new NeuralNetEvaluator((NeuralNetEvaluator) individual.player.getEvaluator(), isMutated));
    }

    public AlphaBetaPlayer getPlayer() {
        return player;
    }

    public synchronized void updateRecordsForMatchWon(){
        roundMatchesPlayed++;
        roundMatchesWon++;
        totalMatchesPlayed++;
        totalMatchesWon++;
    }

    public synchronized void updateRecordsForMatchDrawn(){
        roundMatchesPlayed++;
        roundMatchesDrawn++;
        totalMatchesPlayed++;
        totalMatchesDrawn++;
    }

    public synchronized void updateRecordsForMatchLost(){
        roundMatchesPlayed++;
        roundMatchesLost++;
        totalMatchesPlayed++;
        totalMatchesLost++;
    }

    public void resetRoundScores(){
        roundMatchesPlayed = 0;
        roundMatchesWon = 0;
        roundMatchesDrawn = 0;
        roundMatchesLost = 0;
    }

    public int getTotalScore(){
        return totalMatchesWon - 2*totalMatchesLost;
    }

    public double getAverageTotalScore(){
        return  (double) this.getTotalScore()/totalMatchesPlayed;
    }

    public int getRoundScore(){
        return roundMatchesWon  - 2*roundMatchesLost;
    }

    public int getRoundMatchesPlayed() {
        return roundMatchesPlayed;
    }

    public int getRoundMatchesWon() {
        return roundMatchesWon;
    }

    public int getRoundMatchesLost() {
        return roundMatchesLost;
    }

    public int getRoundMatchesDrawn() {
        return roundMatchesDrawn;
    }

    public int getTotalMatchesPlayed() {
        return totalMatchesPlayed;
    }

    public int getTotalMatchesWon() {
        return totalMatchesWon;
    }

    public int getTotalMatchesLost() {
        return totalMatchesLost;
    }

    public int getTotalMatchesDrawn() {
        return totalMatchesDrawn;
    }

    @Override
    public String toString(){
        StringBuilder details = new StringBuilder();
        details.append("Total: "+totalMatchesPlayed+"-"+totalMatchesWon+" "+totalMatchesDrawn+" "+totalMatchesLost+"\n");
        details.append("Round: "+roundMatchesPlayed+"-"+roundMatchesWon+" "+roundMatchesDrawn+" "+roundMatchesLost+"\n");
        return  details.toString();
    }
}
