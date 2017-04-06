/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.distribution.NormalDistribution;
import stats.DirichletDistribution;
import stats.ExponentialDistribution;
import stats.UniformDistribution;

/**
 *
 * @author sanat
 */
public class TestMain {

    public static void main(String[] args) {
//        double alphaVector[] = new double[]{0.3, 0.65, 0.4, 0.8, 0.4, 0.65, 0.3};
//        DirichletDistribution hyperprior = new DirichletDistribution(alphaVector);
//        for (int i = 0; i < 154; i++) {
//            double[] priorParams = hyperprior.getSample();
//            printArray(new DirichletDistribution(priorParams).getSample(), "0.000");
//        }
//        HashMap<Integer, Integer> counts = new HashMap<>();
//        NormalDistribution nd = new NormalDistribution(8, 3);
//        for (int i = 0; i < 10000; i++) {
//            int sample = (int)(nd.sample()*100);
//            if (counts.containsKey(sample)) {
//                counts.put(sample, counts.get(sample)+1);
//            }
//            else {
//                counts.put(sample, 1);
//            }
//        }
//        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
//            System.out.println(entry.getKey() + "->" + entry.getValue());
//        }
        rewardSamplerTest();
    }

    private static void printArray(double[] array, String format) {
        DecimalFormat df = new DecimalFormat(format);
        for (int i = 0; i < array.length; i++) {
            System.out.print(df.format(array[i]) + " ");
        }
        System.out.println();
    }

    private static void printArray(double[][] array, String format) {
        DecimalFormat df = new DecimalFormat(format);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(df.format(array[i][j]) + " ");
            }
            System.out.println();
        }

        
    }

    private static void rewardSamplerTest() {
        ExponentialDistribution eHypp = new ExponentialDistribution(1);
        double alpha = eHypp.getSample() + 1;   //controls prior over peakiness of reward curve. Exponential because reward is mostly 'peaky' but sometimes flat
        
        UniformDistribution uMeanPrior = new UniformDistribution(0, 6);
        double m = uMeanPrior.getSample();      //controls mean of reward curve
        
        UniformDistribution uVarPrior = new UniformDistribution(0, alpha);
        double v = uVarPrior.getSample();       //controls 'peakiness' of reward curve
        
        ExponentialDistribution rVarPrior = new ExponentialDistribution(200);
        
        double rewardFunction[][] = new double[154][7];
        for (int s = 0; s < 154; s++) {
            double ms = m + (s/11 + s%11)/3.3;
            ms = (ms > 6? ms-7: ms);
//            double ms = (s/11 + s%11)/3.3;
            for (int a = 0; a < 7; a++) {
                double rMean = Math.exp((-(a-ms)*(a-ms))/(2*v)) / Math.sqrt(2*Math.PI*v);
                double rVar = rVarPrior.getSample();
                rewardFunction[s][a] = new NormalDistribution(rMean, Math.sqrt(rVar)).sample();
            }
        }
        
        for (int s = 0; s < rewardFunction.length; s++) {
            double currSMinRew = Arrays.stream(rewardFunction[s]).min().getAsDouble();
//            if(currSMinRew<0) {
                double translator = Math.abs(currSMinRew);
                for(int a = 0; a < rewardFunction[s].length; a++) {
                    rewardFunction[s][a] += translator;
                }
//            }
        }
        
        printArray(rewardFunction, "0.00");
        System.out.println("alpha="+alpha);
        System.out.println("m="+m);
        System.out.println("v=" + v);
        System.out.println("");
    }
}
