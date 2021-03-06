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
import misc.RewardsCParamsSet;
import misc.Utilities;

/**
 *
 * @author sanat
 */
public class SamplesProcessorMain {

    static final int NSTATES = 154;
    static final int NACTIONS = 7;
    static final int NTASKS = 3;
    static final int CUTOFF = 10000;

    public static void main(String[] args) {
        String samplesPath = "output/samples.txt";
        String prunedSamplesPath = "output/prunedsamples/" + new SimpleDateFormat("MM.dd.hh.mm").format(new Date()) + "/";
        new File(prunedSamplesPath).mkdir();
//        List<double[][]> prunedRewardSets = new ArrayList<>();
//        List<Double> logWeights = new ArrayList<>();

//      int lwPos = NSTATES * NACTIONS + 2 * NTASKS + 1;
        int lwPos = 2 * NTASKS + 2;
        
        //two passes required as so much cannot be stored in memory
        //Pass 1: find max log weight
        int linesRead = 0;
        double maxLW = Double.NEGATIVE_INFINITY;
        try (BufferedReader br = new BufferedReader(new FileReader(samplesPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] sample = line.split(" ");
                if (Utilities.isValidDouble(sample[lwPos])) {
                    double currLW = Double.parseDouble(sample[lwPos]);
                    if (currLW > maxLW) {
                        maxLW = currLW;
                    }
                }
                linesRead++;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Lines read before AIOOB, pass 1 = " + linesRead);
        } catch (NumberFormatException nex) {
            System.out.println("Lines read before NFE, pass 1 = " + linesRead);
            nex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Pass 2: Store all reward sets within 'cutoff' difference from max
        HashMap<Double, RewardsCParamsSet> prunedRewardsDistribution = new HashMap<>();
        linesRead = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(samplesPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] sample = line.split(" ");
                if (Utilities.isValidDouble(sample[lwPos])) {
                    double currLW = Double.parseDouble(sample[lwPos]);
                    if (maxLW - currLW < CUTOFF) {
                        //int cursor = NSTATES * NACTIONS + 1;
                        int cursor = 2;
                        double[][] rewards = new double[NTASKS][NSTATES * NACTIONS];
                        double[] cI = new double[NTASKS];
                        for (int i = 0; i < NTASKS; i++) {
                            cI[i] = Double.parseDouble(sample[cursor++]);
                        }

                        for (int i = 0; i < rewards.length; i++) {
                            String r[] = sample[cursor + i].split(",");
                            for (int j = 0; j < rewards[i].length; j++) {
                                rewards[i][j] = Double.parseDouble(r[j]);
                            }
                        }
                        cursor += NTASKS;
                        //if (isValidDouble(sample[cursor])) {
                        prunedRewardsDistribution.put(Double.parseDouble(sample[cursor]), new RewardsCParamsSet(rewards, cI));
//                    prunedRewardSets.add(rewards);
//                    logWeights.add(Double.parseDouble(sample[70]));
                        //}
                    }
                }

                linesRead++;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Lines read before AIOOB, pass 2 = " + linesRead);
        } catch (NumberFormatException nex) {
            nex.printStackTrace();
            System.out.println("Lines read before NFE, pass 2 = " + linesRead);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        double maxLogWeight = prunedRewardsDistribution.keySet().stream().max(Double::compare).get();
        //System.out.println(maxLogWeight);
        prunedRewardsDistribution.keySet()
                .stream()
                .parallel()
                .forEach(x -> {
                    //if(x-maxLogWeight>-10000) {
                    RewardsCParamsSet rcps = prunedRewardsDistribution.get(x);
                    writeToFile(prunedSamplesPath, rcps.getRewards(), rcps.getcParams(), x);
                    //}
                });

    }

    

    private static void writeToFile(String prunedSamplesPath, double[][] rewardSet, double[] cParams, Double logWeight) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(prunedSamplesPath + logWeight + ".txt", true))) {
            for (double[] reward : rewardSet) {
                bw.append(formatReward(reward) + "\n");
            }

            bw.append(logWeight + "\n");
            for (int i = 0; i < NTASKS; i++) {
                bw.append(cParams[i] + "\t");
            }
//            bw.append(cParams[0]+ "\t");
//            bw.append(cParams[1]+ "\t");
//            bw.append(cParams[2]+ "\t");            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatReward(double[] reward) {
        String formatted = "";
        for (int i = 0; i < NSTATES; i++) {
            for (int j = 0; j < NACTIONS; j++) {
                formatted += reward[i * NACTIONS + j] + "\t";
            }
            formatted += "\n";
        }
        return formatted;
    }
}
