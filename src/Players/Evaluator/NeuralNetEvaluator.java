package Players.Evaluator;

import Checkers.CheckersBoard;
import Checkers.Piece;

import java.io.Serializable;
import java.util.Random;

/***
 * NeuralNetEvaluator is a concrete subclass of the abstract Players.Evaluator class.
 * It evaluates the board position by a multi layer feed-forward neural network.
 * The board position is given as an input into a neural network and the output of the neural net is the evaluated value.
 * The Neural Network is comprised of four layers:
 *      (1) Input Layer contains 32 nodes, one for each square on board
 *      (2) First Hidden Layer contains the provided number of nodes
 *      (3) Second Hidden Layer contains provided number of nodes
 *      (4) Output Layer contains a single output node, whose output is the evaluate value of the board
 * NOTE: This class is used in the genetic algorithm, where each individual has its own neural network with its own value of weights of the connections
 *       The weights are updated in the offsprings by making a small random change in each weight
 *       The value of the weights of the connections in the neural network and the value of the king piece are evolvable and update by the genetic algorithm
 */

public class NeuralNetEvaluator implements Serializable, Evaluator {

    private static final int inputNodes = 32;   //final static field holds the number of nods in the input layer
    private int nodeCountHiddenLayer1, nodeCountHiddenLayer2;     //Holds number of nodes in the first and the second hidden layer

    private float kingValue;        //The value of king piece
    private float[][] weightInputToHidden1;         //Weight matrix for connections from the input layer to the first hidden layer
    private float[][] weightHidden1ToHidden2;       //Weight matrix for connections from the first hidden layer to the second hidden layer
    private float[] weightHidden2ToOutput;          //Weight array for connections from the second hidden layer to the output layer

    //Corresponding self adaptive parameters for the weights
    //They control the step size of the search for new mutated parameters of the neural network.
    private float[][] selfAdaptiveParameterForWeightInputToHidden1;
    private float[][] selfAdaptiveParameterForWeightHidden1ToHidden2;
    private float[] selfAdaptiveParameterForWeightHidden2ToOutput;
    /***
     * Parametrized constructor creates a NeuralNetEvaluator object with weights of the connections set to small random values and king value to 2
     * @param nodeCountHiddenLayer1 Number of nodes in the first hidden layer of the neural network
     * @param nodeCountHiddenLayer2 Number of nodes in the second hidden layer of the neural network
     */
    public NeuralNetEvaluator(int nodeCountHiddenLayer1, int nodeCountHiddenLayer2) {
        //Set values for the number of nodes in the first and the second hidden layers
        this.nodeCountHiddenLayer1 = nodeCountHiddenLayer1;
        this.nodeCountHiddenLayer2 = nodeCountHiddenLayer2;

        //Create matrix of appropriate sizes representing the weights of the connections between the different layers
        weightInputToHidden1= new float[inputNodes+1][nodeCountHiddenLayer1];
        weightHidden1ToHidden2 = new float[nodeCountHiddenLayer1+1][nodeCountHiddenLayer2];
        weightHidden2ToOutput = new float[nodeCountHiddenLayer2+1];

        //Create matrix of appropriate sizes representing the self adaptive parameters
        selfAdaptiveParameterForWeightInputToHidden1 = new float[inputNodes+1][nodeCountHiddenLayer1];
        selfAdaptiveParameterForWeightHidden1ToHidden2 = new float[nodeCountHiddenLayer1+1][nodeCountHiddenLayer2];
        selfAdaptiveParameterForWeightHidden2ToOutput = new float[nodeCountHiddenLayer2+1];

        //Intialize selfAdaptiveParameters
        initializeSelfAdaptiveParameter(selfAdaptiveParameterForWeightInputToHidden1);
        initializeSelfAdaptiveParameter(selfAdaptiveParameterForWeightHidden1ToHidden2);
        initializeSelfAdaptiveParameter(selfAdaptiveParameterForWeightHidden2ToOutput);

        //Initialize all the weights of the weight matrices to a small random value from -0.2 to +0.2
        initializeWithRandom(weightInputToHidden1);
        initializeWithRandom(weightHidden1ToHidden2);
        initializeWithRandom(weightHidden2ToOutput);

        //Intialize the king value to a random value from 2 to 3
        kingValue = (float) (Math.random() + 2.0);
    }

    /***
     * Parametrized constructor creates a NeuralNetEvaluator object with weights of the connection set to values
     * with a small random gaussian value difference from the values of the corresponding weight in the given neural network
     * @param parentNeuralNetEvaluator The given parent NeuralNetEvaluator based on whose weights of connections the new NeuralNetEvaluator is to be created
     */
    public NeuralNetEvaluator(NeuralNetEvaluator parentNeuralNetEvaluator, boolean isMutated){
        //Set values for the number of nodes in the first and the second hidden layers
        this.nodeCountHiddenLayer1 = parentNeuralNetEvaluator.nodeCountHiddenLayer1;
        this.nodeCountHiddenLayer2 = parentNeuralNetEvaluator.nodeCountHiddenLayer2;

        //Create matrix of appropriate sizes representing the weights of the connections between the different layers
        weightInputToHidden1= new float[inputNodes+1][nodeCountHiddenLayer1];
        weightHidden1ToHidden2 = new float[nodeCountHiddenLayer1+1][nodeCountHiddenLayer2];
        weightHidden2ToOutput = new float[nodeCountHiddenLayer2+1];

        //Create matrix of appropriate sizes representing the self adaptive parameters
        selfAdaptiveParameterForWeightInputToHidden1 = new float[inputNodes+1][nodeCountHiddenLayer1];
        selfAdaptiveParameterForWeightHidden1ToHidden2 = new float[nodeCountHiddenLayer1+1][nodeCountHiddenLayer2];
        selfAdaptiveParameterForWeightHidden2ToOutput = new float[nodeCountHiddenLayer2+1];

        if(!isMutated){
            //Initialize all the weights with a small random gaussian change in the original neural networks' corresponding weights
            initializeWithGaussianRandomChangeInputToHidden1(parentNeuralNetEvaluator);
            initializeWithGaussianRandomChangeHidden1ToHidden2(parentNeuralNetEvaluator);
            initializeWithGaussianRandomChangeHidden2ToOutput(parentNeuralNetEvaluator);
        }else{
            //Initialize selfAdaptiveParameters
            initializeSelfAdaptiveParameter(selfAdaptiveParameterForWeightInputToHidden1);
            initializeSelfAdaptiveParameter(selfAdaptiveParameterForWeightHidden1ToHidden2);
            initializeSelfAdaptiveParameter(selfAdaptiveParameterForWeightHidden2ToOutput);

            initializeWithRandomChange(parentNeuralNetEvaluator);
        }

        //Initialize the king value with a small random change(-0.1 to +0.1) in the original king value
        this.kingValue = (float) (parentNeuralNetEvaluator.kingValue + Math.random()/5 - 0.1);
    }

    public NeuralNetEvaluator(NeuralNetEvaluator parent1, NeuralNetEvaluator parent2){
        //Set values for the number of nodes in the first and the second hidden layers
        this.nodeCountHiddenLayer1 = parent1.nodeCountHiddenLayer1;
        this.nodeCountHiddenLayer2 = parent2.nodeCountHiddenLayer2;

        //Create matrix of appropriate sizes representing the weights of the connections between the different layers
        weightInputToHidden1= new float[inputNodes+1][nodeCountHiddenLayer1];
        weightHidden1ToHidden2 = new float[nodeCountHiddenLayer1+1][nodeCountHiddenLayer2];
        weightHidden2ToOutput = new float[nodeCountHiddenLayer2+1];

        //Create matrix of appropriate sizes representing the self adaptive parameters
        selfAdaptiveParameterForWeightInputToHidden1 = new float[inputNodes+1][nodeCountHiddenLayer1];
        selfAdaptiveParameterForWeightHidden1ToHidden2 = new float[nodeCountHiddenLayer1+1][nodeCountHiddenLayer2];
        selfAdaptiveParameterForWeightHidden2ToOutput = new float[nodeCountHiddenLayer2+1];

        initializeParametersFromRandomParent(parent1, parent2);

    }

    public NeuralNetEvaluator(float kingValue, float[][] weightInputToHidden1, float[][] weightHidden1ToHidden2, float[] weightHidden2ToOutput, float[][] selfAdaptiveParameterForWeightInputToHidden1, float[][] selfAdaptiveParameterForWeightHidden1ToHidden2, float[] selfAdaptiveParameterForWeightHidden2ToOutput) {
        this.kingValue = kingValue;
        this.weightInputToHidden1 = weightInputToHidden1;
        this.weightHidden1ToHidden2 = weightHidden1ToHidden2;
        this.weightHidden2ToOutput = weightHidden2ToOutput;
        this.selfAdaptiveParameterForWeightInputToHidden1 = selfAdaptiveParameterForWeightInputToHidden1;
        this.selfAdaptiveParameterForWeightHidden1ToHidden2 = selfAdaptiveParameterForWeightHidden1ToHidden2;
        this.selfAdaptiveParameterForWeightHidden2ToOutput = selfAdaptiveParameterForWeightHidden2ToOutput;
        this.nodeCountHiddenLayer1 = weightHidden1ToHidden2.length;
        this.nodeCountHiddenLayer2 = weightHidden2ToOutput.length;
    }

    public float getKingValue() {
        return kingValue;
    }

    public static int getInputNodes() {
        return inputNodes;
    }

    public int getNodeCountHiddenLayer1() {
        return nodeCountHiddenLayer1;
    }

    public int getNodeCountHiddenLayer2() {
        return nodeCountHiddenLayer2;
    }

    public float[][] getWeightInputToHidden1() {
        return weightInputToHidden1;
    }

    public float[][] getWeightHidden1ToHidden2() {
        return weightHidden1ToHidden2;
    }

    public float[] getWeightHidden2ToOutput() {
        return weightHidden2ToOutput;
    }

    public float[][] getSelfAdaptiveParameterForWeightInputToHidden1() {
        return selfAdaptiveParameterForWeightInputToHidden1;
    }

    public float[][] getSelfAdaptiveParameterForWeightHidden1ToHidden2() {
        return selfAdaptiveParameterForWeightHidden1ToHidden2;
    }

    public float[] getSelfAdaptiveParameterForWeightHidden2ToOutput() {
        return selfAdaptiveParameterForWeightHidden2ToOutput;
    }

    @Override
    public float evaluate(CheckersBoard checkersBoard){
        //Get the input from the CheckersBoard object as a 1D array for feeding forward to get the output
        float[] input = getInputArray(checkersBoard);

        //Multiply the input array with the weights matrix of input layer to first hidden layer
        //and then apply the activation function on it to find the output array of the first hidden layer
        float[] outputOfHidden1 = activationFunction(multiply(input, weightInputToHidden1));

        //Multiply the output array of the first hidden layer with the weight matrix of first hidden layer to second hidden layer
        //and then apply the activation function on it to find the output array of the second hidden layer
        float[] outputOfHidden2 = activationFunction(multiply(outputOfHidden1, weightHidden1ToHidden2));

        //Multiply the output array of the second hidden layer with the weight array of second hidden layer to output layer
        //and then apply the activation function on it to find the output of the output layer
        float evaluatedValue = activationFunction(multiply(outputOfHidden2, weightHidden2ToOutput));

        //The output of the neural network thus found is the evaluated value of the board
        return evaluatedValue;
    }

    private void initializeParametersFromRandomParent(NeuralNetEvaluator parent1, NeuralNetEvaluator parent2) {
        Random random = new Random();
        for (int i = 0; i < parent1.weightInputToHidden1.length; i++) {
            for (int j = 0; j < parent1.weightInputToHidden1[0].length; j++) {
                boolean randomBoolean = random.nextBoolean();
                this.weightInputToHidden1[i][j] = randomBoolean? parent1.weightInputToHidden1[i][j] : parent2.weightInputToHidden1[i][j];
                this.selfAdaptiveParameterForWeightInputToHidden1[i][j]
                        = randomBoolean? parent1.selfAdaptiveParameterForWeightInputToHidden1[i][j] : parent2.selfAdaptiveParameterForWeightInputToHidden1[i][j];
            }
        }

        for (int i = 0; i < parent1.weightHidden1ToHidden2.length; i++) {
            for (int j = 0; j < parent1.weightHidden1ToHidden2[0].length; j++) {
                boolean randomBoolean = random.nextBoolean();
                this.weightHidden1ToHidden2[i][j] = randomBoolean? parent1.weightHidden1ToHidden2[i][j] : parent2.weightHidden1ToHidden2[i][j];
                this.selfAdaptiveParameterForWeightHidden1ToHidden2[i][j]
                        = randomBoolean? parent1.selfAdaptiveParameterForWeightHidden1ToHidden2[i][j] : parent2.selfAdaptiveParameterForWeightHidden1ToHidden2[i][j];
            }
        }

        for (int i = 0; i < parent1.weightHidden2ToOutput.length; i++) {
            boolean randomBoolean = random.nextBoolean();
            this.weightHidden2ToOutput[i] = randomBoolean? parent1.weightHidden2ToOutput[i] : parent2.weightHidden2ToOutput[i];
            this.selfAdaptiveParameterForWeightHidden2ToOutput[i]
                    = randomBoolean? parent1.selfAdaptiveParameterForWeightHidden2ToOutput[i] : parent2.selfAdaptiveParameterForWeightHidden2ToOutput[i];
        }

        this.kingValue = random.nextBoolean()? parent1.kingValue : parent2.kingValue;
    }

    private void initializeSelfAdaptiveParameter(float[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++){
                matrix[i][j] = 0.05f;
            }
        }
    }

    private void initializeSelfAdaptiveParameter(float[] array){
        for (int i = 0; i < array.length; i++) {
            array[i] = 0.05f;
        }
    }

    private void initializeWithRandomChange(NeuralNetEvaluator parent){
        Random random = new Random();
        for (int i = 0; i < weightInputToHidden1.length; i++) {
            for (int j = 0; j < weightInputToHidden1[0].length; j++) {
                this.weightInputToHidden1[i][j] = (float) (parent.weightInputToHidden1[i][j] + random.nextGaussian());
            }
        }

        for (int i = 0; i < weightHidden1ToHidden2.length; i++) {
            for (int j = 0; j < weightHidden1ToHidden2[0].length; j++) {
                this.weightHidden1ToHidden2[i][j] = (float) (parent.weightHidden1ToHidden2[i][j] + random.nextGaussian());
            }
        }

        for (int i = 0; i < weightHidden2ToOutput.length; i++) {
            this.weightHidden2ToOutput[i] = (float) (parent.weightHidden2ToOutput[i] + random.nextGaussian());
        }
    }

    /***
     * Sets the weights of the new neural network for the connections from Input Layer to the First Hidden Layer
     * by making a small random gaussian change in the value of weights of the parent neural network
     * and the corresponding step value given by the selfAdaptiveParameter
     * @param parent The parent NeuralNetEvaluator
     */
    private void initializeWithGaussianRandomChangeInputToHidden1(NeuralNetEvaluator parent){
        Random random = new Random();   //Create a random seed
        //Loop through all the individual weights in the weight matrix
        for (int i = 0; i < parent.weightInputToHidden1.length; i++) {
            for (int j = 0; j < parent.weightInputToHidden1[0].length; j++) {
                double randomValue = random.nextGaussian(); //Find a randomly generated gaussian variable
                //Update the self adaptive parameters in the newly created child
                this.selfAdaptiveParameterForWeightInputToHidden1[i][j] = (float) (parent.selfAdaptiveParameterForWeightInputToHidden1[i][j]*Math.exp(0.1103*randomValue));
                //Update the weight in the newly created child
                this.weightInputToHidden1[i][j] = (float) (parent.weightInputToHidden1[i][j] + this.selfAdaptiveParameterForWeightInputToHidden1[i][j]*randomValue);
                //this.weightInputToHidden1[i][j] = (float) (parent.weightInputToHidden1[i][j] + randomValue);
            }
        }
    }

    /***
     * Sets the weights of the new neural network for the connections from First Hidden Layer to the Second Hidden Layer
     * by making a small random gaussian change in the value of weights of the parent neural network
     * and the corresponding step value given by the selfAdaptiveParameter
     * @param parent The parent NeuralNetEvaluator
     */
    private void initializeWithGaussianRandomChangeHidden1ToHidden2(NeuralNetEvaluator parent){
        Random random = new Random();   //Create a random seed
        //Loop through all the individual weights in the weight matrix
        for (int i = 0; i < parent.weightHidden1ToHidden2.length; i++) {
            for (int j = 0; j < parent.weightHidden1ToHidden2[0].length; j++) {
                double randomValue = random.nextGaussian(); //Find a randomly generated gaussian variable
                //Update the self adaptive parameters in the newly created child
                this.selfAdaptiveParameterForWeightHidden1ToHidden2[i][j] = (float) (parent.selfAdaptiveParameterForWeightHidden1ToHidden2[i][j]*Math.exp(0.1103*randomValue));
                //Update the weight in the newly created child
                this.weightHidden1ToHidden2[i][j] = (float) (parent.weightHidden1ToHidden2[i][j] + this.selfAdaptiveParameterForWeightHidden1ToHidden2[i][j]*randomValue);
                //this.weightHidden1ToHidden2[i][j] = (float) (parent.weightHidden1ToHidden2[i][j] + randomValue);
            }
        }
    }

    /***
     * Sets the weights of the new neural network for the connections from Second Hidden Layer to the Output Layer
     * by making a small random gaussian change in the value of weights of the parent neural network
     * and the corresponding step value given by the selfAdaptiveParameter
     * @param parent The parent NeuralNetEvaluator
     */
    private void initializeWithGaussianRandomChangeHidden2ToOutput(NeuralNetEvaluator parent){
        Random random = new Random();   //Create a random seed
        //Loop through all the individual weights in the weight matrix
        for (int i = 0; i < parent.weightHidden2ToOutput.length; i++) {
            double randomValue = random.nextGaussian(); //Find a randomly generated gaussian variable
            //Update the self adaptive parameters in the newly created child
            this.selfAdaptiveParameterForWeightHidden2ToOutput[i] = (float) (parent.selfAdaptiveParameterForWeightHidden2ToOutput[i]*Math.exp(0.1103*randomValue));
            //Update the weight in the newly created child
            this.weightHidden2ToOutput[i] = (float) (parent.weightHidden2ToOutput[i] + this.selfAdaptiveParameterForWeightHidden2ToOutput[i]*randomValue);
            //this.weightHidden2ToOutput[i] = (float) (parent.weightHidden2ToOutput[i]+randomValue);
        }
    }

    /***
     * Initializes all the cells of a 2D matrix with random values from -0.2 to +0.2
     * @param weights The matrix to be initialized
     */
    private void initializeWithRandom(float[][] weights){
        Random random = new Random();   //Create a random seed
        //Loop through all the individual weights in the weight matrix
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                //Get a random value from -0.2 to +0.2 and store it in the weight matrix
                weights[i][j] = (float) (random.nextFloat()*0.4 - 0.2);
            }
        }
    }

    /***
     * Initializes all the cells of a 1D matrix with random values from -0.2 to +0.2
     * @param weights The matrix to be initialized
     */
    private void initializeWithRandom(float[] weights){
        Random random = new Random();   //Create a random seed
        //Loop through all the individual weights in the weight matrix
        for (int i = 0; i < weights.length; i++) {
            //Get a random value from -0.2 to +0.2 and store it in the weight matrix
            weights[i] = (float) (random.nextFloat()*0.4 - 0.2);
        }
    }

    /***
     * Returns a float array as the input matrix for the neural network for a given board position
     * @param checkersBoard The given CheckersBoard object which represents the current board position
     * @return 1D float array representing the input array to be fed into the neural network
     */
    private float[] getInputArray(CheckersBoard checkersBoard){
        float[] input = new float[inputNodes+1];  //Create a 1D float array representing the input for the neural network
        input[0] = 1; //bias
        int index = 1;  //index counter to store values at each index in the input array
        Piece[][] board = checkersBoard.getBoard(); //Get the Piece matrix from the CheckersBoard object
        //Loop through all the squares
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 != 0) {     //Pieces are present only on the squares whose sum of row and column index is odd
                    if(board[i][j] == Piece.WHITE_PAWN)         //For WHITE PAWN input value is +1
                        input[index] = 1;
                    else if(board[i][j] == Piece.BLACK_PAWN)    //For BLACK PAWN input value is -1
                        input[index] = -1;
                    else if(board[i][j] == Piece.WHITE_KING)    //For WHITE KING input value is positive value of the king value according to this neural network
                        input[index] = kingValue;
                    else if(board[i][j] == Piece.BLACK_KING)    //For BLACK KING input value is negative value of the king value according to this neural network
                        input[index] = -1*kingValue;
                    else
                        input[index] = 0;                       //For EMPTY space input value is 0
                    index++;
                }
            }
        }

        return input;
    }

    /***
     * Apply activation function i.e. tanh() on each element of a 1D array
     * @param A The 1D array on which activation function is to be applied
     * @return The new 1D array with elements as value after the activation is applied on that element of the given array
     */
    private static float[] activationFunction(float[] A){
        float[] result = new float[A.length];   //Create a 1D float array to store the resulting array after applying the activation function
        //Loop through all the elements of the array
        for (int i = 0; i < A.length; i++) {
            result[i] = (float) Math.tanh(A[i]);    //Apply tanh() on each element
        }
        return result;
    }

    /***
     * Apply activation function i.e. tanh() on one element
     * @param x The element on which activation function is to be applied
     * @return The value after the activation is applied on the given element
     */
    public static float activationFunction(float x){
        return (float) Math.tanh(x);
    }

    /***
     * Multiplies two 2D matrices and returns the product matrix
     * @param A First matrix in the product
     * @param B Second matrix in the product
     * @return The product matrix found after multiplying first matrix by the second matrix
     */
    private static float[] multiply(float[] A, float[][] B){
        float[] P = new float[B[0].length+1];
        P[0] = 1;
        for(int i=0; i<B[0].length; i++){
            float sum = 0;
            for (int j = 0; j < A.length; j++) {
                sum += A[j]*B[j][i];
            }
            P[i+1] = sum;
        }
        return P;
    }

    /***
     * Multiplies two 1D matrices and returns their dot product
     * @param A First matrix in the product
     * @param B Second matrix in the product
     * @return The dot product found after multiplying first matrix by the second matrix
     */
    private static float multiply(float[] A, float[] B){
        float product = 0;
        for (int i = 0; i < A.length; i++) {
            product += A[i]*B[i];
        }
        return product;
    }
}
