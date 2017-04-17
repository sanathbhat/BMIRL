/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sanat
 */
public class ExpectedRewardCalculator {

    static final int NTASKS = 3;
    static final int NSTATES = 154;
    static final int NACTIONS = 7;

    public static void main(String[] args) {
        String prunedSamplesPath = "output/prunedsamples/04.14.12.28";// + args[0];
        File samplesFolder = new File(prunedSamplesPath);

        //find max logweight and normalizer
        double maxLW = Double.NEGATIVE_INFINITY;
        for (File sampleFile : samplesFolder.listFiles()) {
            double currLW = Double.parseDouble(sampleFile.getName().substring(0, sampleFile.getName().length() - 4));
            if (currLW > maxLW) {
                maxLW = currLW;
            }
        }

        double lse = 0;
        for (File sampleFile : samplesFolder.listFiles()) {
            double currLW = Double.parseDouble(sampleFile.getName().substring(0, sampleFile.getName().length() - 4));
            lse += Math.exp(currLW - maxLW);
        }
        lse = maxLW + Math.log(lse);
        double Z = Math.exp(lse);

        //List<double[][][]> rewardFunctionSetList = new ArrayList<>();
        double expectedRF[][][] = new double[NTASKS][NSTATES][NACTIONS];

        for (File sampleFile : samplesFolder.listFiles()) {
            double currLW = Double.parseDouble(sampleFile.getName().substring(0, sampleFile.getName().length() - 4));
            double currWeight = Math.exp(currLW) / Z;
            System.out.println(currWeight);
            //double rewardFunctionSet[][][] = new double[NTASKS][NSTATES][NACTIONS];
            try (BufferedReader br = new BufferedReader(new FileReader(sampleFile))) {
                String line;
                for (int m = 0; m < NTASKS; m++) {
                    for (int s = 0; s < NSTATES; s++) {
                        line = br.readLine();
                        String rewardValues[] = line.split("\t");
                        for (int a = 0; a < NACTIONS; a++) {
                            //rewardFunctionSet[m][s][a] = Double.parseDouble(rewardValues[a]);
                            expectedRF[m][s][a] += (currWeight * Double.parseDouble(rewardValues[a]));
                        }
                    }
                    line = br.readLine();
                }
                //rewardFunctionSetList.add(rewardFunctionSet);                
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        for (int m = 0; m < NTASKS; m++) {
            for (int s = 0; s < NSTATES; s++) {
                for (int a = 0; a < NACTIONS; a++) {
                    System.out.print(expectedRF[m][s][a] + "\t");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
