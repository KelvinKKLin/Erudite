package ca.mcmaster.plan6.erudite.fetch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Puru on 2017-04-03.
 * Modified by Kelvin on 2017-04-04.
 */
public class StatisticsCalculator {

    //Variable Declarations
    private double tol = 0.00000000001; //Floating Point Tolerance

    /**
     * This method calculates the mode of a collection of numbers.
     * @param array An array of numbers
     * @return  A set of numbers denoting the mode
     */
    public HashSet<Double> computeMode(ArrayList<Double> array) {

        //Variable Instantiation
        HashSet<Double> modes = new HashSet<Double>();
        int maxCount = 1;

        //Check to see if input is empty
        if(array.size() == 0){
            return modes;
        }

        //Iteratively search for the mode
        for(int i = 0; i < array.size(); i++){
            int currentMax = 1;

            for(int j = 0; j < array.size(); j++){
                if(Math.abs((array.get(i) - array.get(j))) < tol && i!=j){
                    currentMax++;
                }
            }

            //Check to see if mode is found
            if(currentMax == maxCount){
                modes.add(array.get(i));
            } else if(currentMax > maxCount){
                modes.clear();
                maxCount = currentMax;
                modes.add(array.get(i));
            }
        }

        //Return the mode
        return modes;
    }

    /**
     * This method computes the mean of a collection of numbers.
     * @param array a collection of numbers
     * @return  The mean of the collection
     */
    public double computeMean (ArrayList<Double> array){

        //Variable Instantiation
        double mean = 0;

        //Check to see if input is empty
        if(array.size() == 0){
            return -1;
        }

        //Sum all of the elements
        for(Double d : array){
            mean += d;
        }

        //Compute the mean
        mean /= array.size();

        //Return the mean
        return mean;
    }

    /**
     * This method computes the median of a given collection of numbers
     * @param array An array of numbers
     * @return  The median of the numbers
     */
    public double computeMedian(ArrayList<Double> array){

        //Variable Declarations
        double median;

        //Check to see if input is empty
        if(array.size() == 0){
            return -1;
        }

        //Sort the array
        Collections.sort(array);

        //Compute the middle location
        int middle = array.size()/2;

        //Compute the median
        if(array.size() % 2 == 1){
            median = array.get(middle);
        }
        else{
            median = (array.get(middle-1) + array.get(middle)) / 2;
        }

        //Return the median
        return median;
    }

    /**
     * This method computes the population standard deviation.
     * @param array a collection of numbers
     * @return  The population standard deviation
     */
    public double stdDeviation(ArrayList<Double> array){

        //Check to see if input is empty
        if(array.size() == 0){
            return -1;
        }

        //Compute the standard deviation
        double stdDeviation = Math.sqrt(computeVariance(array));

        //Return the standard deviation
        return stdDeviation;
    }

    /**
     * This method computes the variance for a collection of numbers
     * @param array a collection of numbers
     * @return  The variance of the numbers
     */
    public double computeVariance(ArrayList<Double> array){

        //Variable Declaration
        double variance = 0;

        //Check to see if input is empty
        if(array.size() == 0){
            return -1;
        }

        //Compute the mean
        double mean = computeMean(array);

        //Compute the variance
        for (int i = 0; i < array.size(); i++){
            variance += ((array.get(i)-mean) * (array.get(i) - mean));
        }
        variance = variance /array.size();

        //Return the variance
        return variance;
    }

}
