/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import mdp.RewardFunction;


/**
 *
 * @author sbhat
 */
public class WeightedSample {
    private double beta;      //parameter of the softmax prior over the policy's c parameter
    private double[] alpha;   //the parameter(s) for the reward prior
    private final List<Double> cParams;
    private final List<RewardFunction> rewardSet;
    private double logWeight;
    static BufferedWriter bw; 
    
    public WeightedSample() {
        this.cParams = new ArrayList<>();
        this.rewardSet = new ArrayList<>();
    }

//    public WeightedSample(List<RewardFunction> rewardSet, double weight) {
//        this.rewardSet = rewardSet;
//        this.logWeight = weight;
//    }
    
    public void appendSampleToFile() {
        DecimalFormat doubleFormatter = new DecimalFormat("0.000");
        String line = "";
        line += doubleFormatter.format(beta) + " ";             //0
        
        for (int i = 0; i < alpha.length; i++) {
            line += doubleFormatter.format(alpha[i]) + ",";     //1
        }
        line += " ";
        
        for (Double cI : cParams) {
            line += doubleFormatter.format(cI) + " ";           //1079, 1080, 1081      //2, 3, 4
        }
        
        for (RewardFunction rewardFunction : rewardSet) {
            line += rewardFunction + " ";                       //1082, 1083, 1084 (csv of 1078 values each)    //5, 6, 7
        }
        
        line += doubleFormatter.format(logWeight) + "\n";       //1085      //8
        
        try {
            //if(new java.io.File(fileName).exists())
            if(!Double.isNaN(logWeight)) {
                synchronized(bw) {
                    bw.append(line);
                }
            }
            //else
                //bw.write(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addReward(RewardFunction rewardFunction) {
        rewardSet.add(rewardFunction);
    }

    /**
     * @param weight the logWeight to set
     */
    public void setLogWeight(double weight) {
        this.logWeight = weight;
    }

    /**
     * @return the rewardSet
     */
    public List<RewardFunction> getRewardSet() {
        return rewardSet;
    }

    /**
     * @return the logWeight
     */
    public double getLogWeight() {
        return logWeight;
    }

    /**
     * @return the beta
     */
    public double getBeta() {
        return beta;
    }

    /**
     * @param beta the beta to set
     */
    public void setBeta(double beta) {
        this.beta = beta;
    }

    /**
     * @return the alpha
     */
    public double[] getAlpha() {
        return alpha;
    }

    /**
     * @param alpha the alpha to set
     */
    public void setAlpha(double... alpha) {
        this.alpha = new double[alpha.length];
        System.arraycopy(alpha, 0, this.alpha, 0, alpha.length);
    }

    /**
     * @return the cParams
     */
    public List<Double> getCParams() {
        return cParams;
    }

    /**
     * @param c
     */
    public void addCParam(double c) {
        cParams.add(c);
    }
        
}
