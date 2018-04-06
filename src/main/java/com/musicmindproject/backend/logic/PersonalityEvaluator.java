package com.musicmindproject.backend.logic;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class PersonalityEvaluator {
    @Inject
    private QuestionManager questionManager;
    private double[][] factors;
    private static PersonalityEvaluator instance;


    public static PersonalityEvaluator getInstance(){
        if(instance == null)
            instance = new PersonalityEvaluator();
        return instance;
    }

    @PostConstruct
    private void init(){
        factors = questionManager.getFactors();
    }

    /**
     * @param inputs double array of all the answer values received by the PersonalizerEndpoint
     * @return the calculated personality values
     */
    public double[] getOutputs(double[] inputs){
        double[] outputs = new double[5];
        outputs[0] = computeResults(inputs, 0);
        outputs[3] = computeResults(inputs, 1);
        outputs[4] = computeResults(inputs, 2);
        outputs[2] = computeResults(inputs, 3);
        outputs[1] = computeResults(inputs, 4);
        return outputs;
    }

    /**
     * @param inputs double array of all the answer values received by the PersonalizerEndpoint
     * @param type personality type that should be calculated
     * @return calculated value for the requested personality type
     */
    private double computeResults(double[] inputs, int type){

        if(inputs.length == factors[type].length){
            double productSum = 0;
            //This for-loop goes through every input and calculates its productsum using the predetermined factors
            //productsum is the sum of all products of inputs and the correlating factors
            for(int i = 0; i < inputs.length; i++){
                if(factors[type][i] > 0){
                    productSum += inputs[i]*factors[type][i];
                }
                //since some of the factors are negative the inputs have to be swapped
                else if(factors[type][i] < 0){
                    double factor = factors[type][i]*(-1);
                    double input = inputs[i] == 4 ? 0 : inputs[i] == 3 ? 1 : inputs[i] == 1 ? 3 : inputs[i] == 0 ? 4 : 0;
                    productSum += input*factor;
                }

            }
            double factorSum = 0;
            //calculates the normalized sum of all the factors
            for(int i = 0; i < factors[type].length; i++){
                if(factors[type][i] < 0){
                    factorSum += factors[type][i]*(-1);
                }
                else
                    factorSum += factors[type][i];
            }

            double result = productSum/factorSum;

            if(result < 0)
                result = 0;
            else if(result > 4)
                result = 4;

            return mapResult(result);
        }
        return -1;
    }

    /**
     * @param value result value that should be mapped
     * @return the value mapped from 0|4 to 0|100
     */
    private double mapResult(double value)
    {
        return (value - (double) 0) / ((double) 4 - (double) 0) * ((double) 100 - (double) 0) + (double) 0;
    }
}
