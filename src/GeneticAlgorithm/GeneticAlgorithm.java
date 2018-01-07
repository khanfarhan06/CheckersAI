package GeneticAlgorithm;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public abstract class GeneticAlgorithm implements Serializable{
    ArrayList<Individual> population;
    private int generationCount;
    protected final int individualCount;
    private final int maxNumberOfGenerations;
    int matchesPerTournament;

    protected GeneticAlgorithm(int individualCount, int maxNumberOfGenerations, int depth, int nodeCountHiddenLayer1, int nodeCountHiddenLayer2, int matchesPerTournament){
        this.individualCount = individualCount;
        this.maxNumberOfGenerations = maxNumberOfGenerations;
        this.matchesPerTournament = matchesPerTournament;
        generationCount=1;
        initializeWithRandomIndividuals(depth, nodeCountHiddenLayer1, nodeCountHiddenLayer2);
    }

    protected GeneticAlgorithm(GeneticAlgorithm geneticAlgorithm, int moreGenerations){
        this.individualCount = geneticAlgorithm.individualCount;
        this.maxNumberOfGenerations = geneticAlgorithm.maxNumberOfGenerations + moreGenerations;
        this.generationCount = geneticAlgorithm.generationCount;
        this.population = geneticAlgorithm.population;
        this.matchesPerTournament = geneticAlgorithm.matchesPerTournament;
    }

    public ArrayList<Individual> getPopulation() {
        return population;
    }

    public void start(){
        while(generationCount<=maxNumberOfGenerations){
            System.out.println("Generation : " +generationCount);
            populateWithNewIndividuals();

            playTournament();

            sortIndividuals();

            killWeakIndividuals();

            showPopulationDetails();

            resetRoundScores();

            generationCount++;
        }
    }

    private void showPopulationDetails(){
        for(Individual individual: population){
            System.out.println(individual.toString());
        }
    }

    private void initializeWithRandomIndividuals(int depth, int nodeCountHiddenLayer1, int nodeCountHiddenLayer2){
        this.population = new ArrayList<Individual>();
        for (int i = 0; i < individualCount; i++) {
            population.add(new Individual(depth, nodeCountHiddenLayer1, nodeCountHiddenLayer2));
        }
    }

    protected abstract void playTournament();

    private void sortIndividuals(){
        Collections.sort(population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                int o1TotalScore = (int)(o1.getAverageTotalScore()*1000);
                int o2TotalScore = (int)(o2.getAverageTotalScore()*1000);
                if(o1TotalScore == o2TotalScore)
                    return o2.getRoundScore() - o2.getRoundScore();
                return o2TotalScore-o1TotalScore;
            }
        });
    }

    private void killWeakIndividuals(){
        for (int i = 0; i < individualCount; i++) {
            population.remove(population.size()-1);
        }
    }

    private void populateWithNewIndividuals(){
        //Random random = new Random();
        for (int i = 0; i < individualCount; i++) {
            Individual parent = population.get(i);
            //double randomValue = random.nextDouble();
            population.add(new Individual(parent, false));
        }
    }

    private void resetRoundScores(){
        for(Individual individual: population)
            individual.resetRoundScores();
    }

    public void saveObjectToFile(String filename){
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
