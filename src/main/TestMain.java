/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.distribution.NormalDistribution;
import stats.DirichletDistribution;
import stats.ExponentialDistribution;
import stats.ProductDirichletDistribution;
import stats.UniformDistribution;

/**
 *
 * @author sanat
 */
public class TestMain {

    public static void main(String[] args) {
//        ExponentialDistribution hp = new ExponentialDistribution(5);
//        for (int i = 0; i < 1000; i++) {
//            ExponentialDistribution p = new ExponentialDistribution(hp.getSample());
//            for (int j = 0; j < 3; j++) {
//                System.out.print(p.getSample() + "\t");
//            }
//            System.out.println();
//        }

        Random r = new Random();
        double alpha[] = new double[1078];
        for (int i = 0; i < alpha.length; i++) {
            alpha[i] = r.nextDouble();
        }

        for (int i = 0; i < 1000; i++) {
            ProductDirichletDistribution pdd = new ProductDirichletDistribution(alpha, 1, 1078);
            pdd.getSample();
        }

//        
//        for (int i = 0; i < 1000; i++) {
//            for (int j = 0; j < alpha.length; j++) {
//                DirichletDistribution dd = new DirichletDistribution(alpha[j]);
//                dd.getSample();
//            }
//        }
//        //rewardSamplerTest();
    }

    private static void lineRemover() {
        String samplesPath = "output/samples.txt";
        String newPath = "output/samples2.txt";
        int linesRead = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(samplesPath))) {
            String line;
            BufferedWriter bw = new BufferedWriter(new FileWriter(newPath, true));
            while ((line = br.readLine()) != null && linesRead < 500000) {
                linesRead++;
                bw.write(line + "\n");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Lines read before AIOOB, pass 1 = " + linesRead);
        } catch (NumberFormatException nex) {
            System.out.println("Lines read before NFE, pass 1 = " + linesRead);
            nex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            double ms = m + (s / 11 + s % 11) / 3.3;
            ms = (ms > 6 ? ms - 7 : ms);
//            double ms = (s/11 + s%11)/3.3;
            for (int a = 0; a < 7; a++) {
                double rMean = Math.exp((-(a - ms) * (a - ms)) / (2 * v)) / Math.sqrt(2 * Math.PI * v);
                double rVar = rVarPrior.getSample();
                rewardFunction[s][a] = new NormalDistribution(rMean, Math.sqrt(rVar)).sample();
            }
        }

        for (int s = 0; s < rewardFunction.length; s++) {
            double currSMinRew = Arrays.stream(rewardFunction[s]).min().getAsDouble();
//            if(currSMinRew<0) {
            double translator = Math.abs(currSMinRew);
            for (int a = 0; a < rewardFunction[s].length; a++) {
                rewardFunction[s][a] += translator;
            }
//            }
        }

        printArray(rewardFunction, "0.00");
        System.out.println("alpha=" + alpha);
        System.out.println("m=" + m);
        System.out.println("v=" + v);
        System.out.println("");
    }
}
