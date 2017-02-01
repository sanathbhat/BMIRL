/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author sanat
 */
public class SamplesProcessorMain {
    static final int NSTATES = 9;
    static final int NACTIONS = 7;
    static final int NTASKS = 3;

    public static void main(String[] args) {
        String samplesPath = "output/samples.txt";
        String prunedSamplesPath = "output/prunedsamples/" + new SimpleDateFormat("MM.dd.hh.mm").format(new Date()) + "/";
        new File(prunedSamplesPath).mkdir();
//        List<double[][]> prunedRewardSets = new ArrayList<>();
//        List<Double> logWeights = new ArrayList<>();
        HashMap<Double, double[][]> prunedRewardsDistribution = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(samplesPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] sample = line.split(" ");
                double[][] rewards = new double[NTASKS][NSTATES*NACTIONS];
                for (int i = 0; i < rewards.length; i++) {
                    String r[] = sample[67 + i].split(",");
                    for (int j = 0; j < rewards[i].length; j++) {
                        rewards[i][j] = Double.parseDouble(r[j]);
                    }
                }
                if (isValidDouble(sample[70])) {
                    prunedRewardsDistribution.put(Double.parseDouble(sample[70]), rewards);
//                    prunedRewardSets.add(rewards);
//                    logWeights.add(Double.parseDouble(sample[70]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        double maxLogWeight = prunedRewardsDistribution.keySet().stream().max(Double::compare).get();
        //System.out.println(maxLogWeight);
        
        prunedRewardsDistribution.keySet()
                .stream()
                .forEach(x -> {
                    if(x-maxLogWeight>-10000) 
                        writeToFile(prunedSamplesPath, prunedRewardsDistribution.get(x), x);
                });
        
    }

    private static boolean isValidDouble(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if(!Character.isDigit(c) && c != '.' && c != '-')
                return false;
        }
        return true;        
    }

    private static void writeToFile(String prunedSamplesPath, double[][] rewardSet, Double logWeight) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(prunedSamplesPath + logWeight + ".txt", true))) {
            for (double[] reward : rewardSet) {
                bw.append(formatReward(reward) + "\n");
            }
            bw.append(logWeight+"");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatReward(double[] reward) {
        String formatted = "";
        for (int i = 0; i < NSTATES; i++) {
            for (int j = 0; j < NACTIONS; j++) {
                formatted += reward[i*NACTIONS + j] + "\t";
            }
            formatted += "\n";
        }
        return formatted;
    }
}
