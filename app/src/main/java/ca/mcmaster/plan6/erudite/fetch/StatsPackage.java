package ca.mcmaster.plan6.erudite.fetch;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Created by Puru on 2017-04-03.
 */

public class StatsPackage {

    //cannot get multiple mode values
    //most popular
    public static int mode(ArrayList<Integer> array)
    {

        //converts array list to array
        Integer[] a = new Integer[array.size()];
        a = array.toArray(a);


        int count = 1, tempCount;
        int mode = a[0];
        int temp = 0;
        for (int i = 0; i < (a.length - 1); i++)
        {
            temp = a[i];
            tempCount = 0;
            for (int j = 1; j < a.length; j++)
            {
                if (temp == a[j])
                    tempCount++;
            }
            if (tempCount > count)
            {
                mode = temp;
                count = tempCount;
            }
        }
        return mode;
    }
    //average
    public static float mean (ArrayList<Integer> array){
        //converts array list to array
        Integer[] a = new Integer[array.size()];
        a = array.toArray(a);

        float sum = 0;
        float mean;

        for(int i = 0; i < a.length;i++){
            sum += a[i];
        }

        mean = sum/a.length;

        return mean;
    }

    //middle value
    public static int median(ArrayList<Integer> array)
    {
        //converts array list to array
        Integer[] a = new Integer[array.size()];
        a = array.toArray(a);

        Arrays.sort(a);
        int middle = a.length/2;
        int median = 0;
        if(a.length%2 == 1){
            median = a[middle];
        }
        else{
            median = (a[middle-1] + a[middle]) / 2;
        }

        return median;
    }

    //population standard deviation
    public static float stdDeviation(ArrayList<Integer> array){
        float mean = mean(array);
        //converts array list to array
        Integer[] a = new Integer[array.size()];
        a = array.toArray(a);
        float sum = 0;

        for (int i = 0; i < a.length; i++){
            sum += ((a[i]-mean) * (a[i] - mean));
        }

        float avgofSum = sum /a.length;
        float stdDeviation = (float) Math.sqrt(avgofSum);


        return stdDeviation;
    }

    //Variance (population standard deviation)
    public static float Variance(ArrayList<Integer> array){
        float variance = stdDeviation(array) * stdDeviation(array);

        return variance;
    }


}
