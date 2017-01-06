/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mdp.RewardFunction;

/**
 *
 * @author sbhat
 */
public class JointRewardDistribution {

    private static final double EPSILON = Math.pow(10, -5);
    private final RewardFunction[][] support;
    private final double[] probability;
    private final int n;

    JointRewardDistribution(HashMap<List<RewardFunction>, Double> sampleWeightsMap, /*HashMap<List<RewardFunction>, Integer> sampleCounts, */ int m) {
        n = sampleWeightsMap.size();    //the number of discrete support points i.e. x axis values

        //precision required when computing normalized likelihood from log likelihood
        double cutoff = Math.log(EPSILON) - Math.log(n);

        support = new RewardFunction[n][m];
        probability = new double[n];

        double lMax = Double.NEGATIVE_INFINITY;

        int i = 0;
        for (Map.Entry<List<RewardFunction>, Double> entry : sampleWeightsMap.entrySet()) {
            List<RewardFunction> rewardComb = entry.getKey();
            for (int j = 0; j < m; j++) {
                support[i][j] = rewardComb.get(j);
            }
            double l = entry.getValue();
            probability[i] = l;         //set pi = li
            if (l > lMax) {
                lMax = l;
            }
            i++;
        }

        //now set actual px. 
        //px = e^(lx-lMax) if (lx-lMax) >= cutoff
        //   = 0           otherwise
        double pSum = 0;
        for (int x = 0; x < n; x++) {
            double lx = probability[x];
            if (lx - lMax >= cutoff) {
                probability[x] = Math.exp(lx - lMax);
            } else {
                probability[x] = 0;
            }
            pSum += probability[x];
        }

        //normalize
        for (int x = 0; x < n; x++) {
            probability[x] /= pSum;
        }

    }

    /* When sample weights are actual, not log, weights
    JointRewardDistribution(HashMap<List<RewardFunction>, Double> sampleWeightsMap, HashMap<List<RewardFunction>, Integer> sampleCounts, int m) {
        int n = sampleWeightsMap.size();    //the number of discrete support points i.e. x axis values
        support = new RewardFunction[n][m];
        probability = new double[n];
        double weightSum = 0;
        int i = 0;
        for (Map.Entry<List<RewardFunction>, Double> entry : sampleWeightsMap.entrySet()) {
            List<RewardFunction> rewardComb = entry.getKey();
            for (int j = 0; j < m; j++) {
                support[i][j] = rewardComb.get(j);
            }
            probability[i] = entry.getValue();
            weightSum += probability[i];
            i++;
        }
        
        for (int x = 0; x < n; x++) {
            probability[x] /= weightSum;
        }
    }*/
    public void displayDistribution() {
        DecimalFormat df = new DecimalFormat("0.000");
        for (int i = 0; i < n; i++) {
            if (probability[i] > 0) {
                for (int j = 0; j < support[i].length; j++) {
                    System.out.print(support[i][j] + "\t");
                }
                System.out.println(probability[i]);
            }
        }
    }

    public void writeToFile(String filePath) {
        DecimalFormat df = new DecimalFormat("0.000");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < n; i++) {
                if (probability[i] > 0) {
                    for (int j = 0; j < support[i].length; j++) {
                        bw.write(support[i][j] + "\t");
                    }
                    bw.write(probability[i] + "\n");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
