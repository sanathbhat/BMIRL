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
import misc.Utilities;

/**
 * Processes samples file and generates expected reward at the same time
 * @author sanat
 */
public class NewSamplesProcessor {

    static final int NSTATES = 154;
    static final int NACTIONS = 7;
    static final int NTASKS = 3;
    static final double CUTOFF = 2;

    public static void main(String[] args) {
        String samplesPath = "output/samples.txt";
        String prunedSamplesPath = "output/prunedsamples/" + new SimpleDateFormat("MM.dd.hh.mm").format(new Date()) + "/";
        new File(prunedSamplesPath).mkdir();
        int samplesConsideredInExpectedValue = 0;
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

        //Pass 2: Find lse using all rewards within cutoff distance from maxLW reward
        //HashMap<Double, RewardsCParamsSet> prunedRewardsDistribution = new HashMap<>();
        //List<Map.Entry<Double, RewardsCParamsSet>> prunedRewardsDistributionList = new ArrayList<>();
        double lse = 0;
        double tlse = 0;
        linesRead = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(samplesPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] sample = line.split(" ");
                if (Utilities.isValidDouble(sample[lwPos])) {
                    double currLW = Double.parseDouble(sample[lwPos]);
                    if (maxLW - currLW < CUTOFF) {
                        lse += Math.exp(currLW - maxLW);
                    }
                    tlse += Math.exp(currLW - maxLW);
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
        lse = maxLW + Math.log(lse);
        tlse = maxLW + Math.log(tlse);
        //double Z = Math.exp(lse);
        System.out.println("MaxLW = "+maxLW);
        System.out.println("LSE ="+lse);
        
        //Pass 3: Compute expected reward
        double expectedRF[][] = new double[NTASKS][NSTATES*NACTIONS];
        double expectedCI[] = new double[NTASKS];
        double expectedAlpha[] = new double[NSTATES];     //type 2
//        double expectedAlpha[] = new double[1];           //type 3
//        double expectedAlpha[] = new double[NSTATES/11];    //type 4
        linesRead = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(samplesPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] sample = line.split(" ");
                if (Utilities.isValidDouble(sample[lwPos])) {
                    double currLW = Double.parseDouble(sample[lwPos]);
                    if (maxLW - currLW < CUTOFF) {
                        samplesConsideredInExpectedValue++;
                        double currWeight = Math.exp(currLW-lse);
                        /*Expected alpha calculation*/
                        int cursor = 1;
                        String alpha[] = sample[cursor++].split(",");
                        for (int i = 0; i < alpha.length; i++) {
                            expectedAlpha[i] += (currWeight * Double.parseDouble(alpha[i]));
                        }
                        /*Expected cI calculation*/
//                        double[] cI = new double[NTASKS];
                        for (int m = 0; m < NTASKS; m++) {
//                            cI[m] = Double.parseDouble(sample[cursor++]);
                            expectedCI[m] += (currWeight * Double.parseDouble(sample[cursor++]));
                        }
                        
                        /*Expected reward calculation*/
//                        int cursor = 5;
                        for (int m = 0; m < NTASKS; m++) {
                            String taskMReward[] = sample[cursor + m].split(",");
                            for (int r = 0; r < taskMReward.length; r++) {
//                                rewards[i][j] = Double.parseDouble(r[j]);
                                expectedRF[m][r] += (currWeight * Double.parseDouble(taskMReward[r]));
                            }
                        }
                    }
                }

                linesRead++;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Lines read before AIOOB, pass 3 = " + linesRead);
        } catch (NumberFormatException nex) {
            nex.printStackTrace();
            System.out.println("Lines read before NFE, pass 3 = " + linesRead);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeToFile(prunedSamplesPath, expectedRF, expectedCI, expectedAlpha, 0);
//        for (int m = 0; m < NTASKS; m++) {
//            for (int s = 0; s < NSTATES; s++) {
//                for (int a = 0; a < NACTIONS; a++) {
//                    System.out.print(expectedRF[m][s*NACTIONS+a] + "\t");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }
//        
//        for (int m = 0; m < NTASKS; m++) {
//            System.out.print(expectedCI[m] + "\t");
//        }
        
        System.out.println("\nSamples Considered = "+samplesConsideredInExpectedValue);
        System.out.println("Total weight accorded by used samples = " + Math.exp(lse-tlse));
    }

    

    private static void writeToFile(String prunedSamplesPath, double[][] rewardSet, double[] cParams, double alpha[], double logWeight) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(prunedSamplesPath + "newsample.txt", true))) {
            for (double[] reward : rewardSet) {
                bw.append(formatReward(reward) + "\n");
            }

            bw.append(logWeight + "\n");
            for (int i = 0; i < NTASKS; i++) {
                bw.append(cParams[i] + "\t");
            }
            bw.append("\n\n");
            for (int i = 0; i < alpha.length; i++) {
                 bw.append(i + "\t" +alpha[i] + "\n");
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
