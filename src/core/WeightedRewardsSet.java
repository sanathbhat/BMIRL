/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.ArrayList;
import java.util.List;
import mdp.RewardFunction;


/**
 *
 * @author sbhat
 */
public class WeightedRewardsSet {
    private final List<RewardFunction> rewardSet;
    private double logWeight;

    public WeightedRewardsSet() {
        this.rewardSet = new ArrayList<>();
        this.logWeight = 0;
    }

    public WeightedRewardsSet(List<RewardFunction> rewardSet, double weight) {
        this.rewardSet = rewardSet;
        this.logWeight = weight;
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
    
    
    
}
