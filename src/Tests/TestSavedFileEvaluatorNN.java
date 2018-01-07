package Tests;

import Players.Evaluator.NeuralNetEvaluator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestSavedFileEvaluatorNN {
    public static void main(String[] args) {
        List<NeuralNetEvaluator> evaluators = readEvaluatorNN("./src/Data/goodEvaluatorNN.dat");
        for(NeuralNetEvaluator neuralNetEvaluator : evaluators){
            System.out.println(neuralNetEvaluator.getKingValue());
        }
    }

    private static List<NeuralNetEvaluator> readEvaluatorNN(String filepath) {
        List<NeuralNetEvaluator> neuralNetEvaluators = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(filepath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            int count = objectInputStream.readInt();
            for (int i = 0; i < count; i++) {
                neuralNetEvaluators.add((NeuralNetEvaluator) objectInputStream.readObject());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return neuralNetEvaluators;
    }


}
