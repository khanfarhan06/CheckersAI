package Tests;

import Players.Evaluator.NeuralNetEvaluator;
import GeneticAlgorithm.GeneticAlgorithmPlayingNeuralNet;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Individual;

import java.io.*;

public class TestSaveGoodEvaluatorNN {
    public static void main(String[] args) {
        GeneticAlgorithmPlayingNeuralNet geneticObject = (GeneticAlgorithmPlayingNeuralNet) readGeneticObject("./src/Data/object250genNN2LossAvgTotal.dat");
        Individual individual1 = geneticObject.getPopulation().get(0);
        Individual individual2 = geneticObject.getPopulation().get(4);
        NeuralNetEvaluator neuralNetEvaluator1 = (NeuralNetEvaluator) individual1.getPlayer().getEvaluator();
        NeuralNetEvaluator neuralNetEvaluator2 = (NeuralNetEvaluator) individual2.getPlayer().getEvaluator();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./src/Data/goodEvaluatorNN.dat");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeInt(2);
            objectOutputStream.writeObject(neuralNetEvaluator1);
            objectOutputStream.writeObject(neuralNetEvaluator2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static GeneticAlgorithm readGeneticObject(String filepath){
        try {
            FileInputStream fileInputStream = new FileInputStream(filepath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (GeneticAlgorithm) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
