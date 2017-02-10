/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

/**
 *
 * @author sanat
 */
public class RewardsCParamsSet {
    private final double[][] rewards;
    private final double[] cParams;
    
    /**
     * @return the rewards
     */
    public double[][] getRewards() {
        return rewards;
    }

    /**
     * @return the cParams
     */
    public double[] getcParams() {
        return cParams;
    }

    public RewardsCParamsSet(double[][] rewards, double[] cParams) {
        this.rewards = rewards;
        this.cParams = cParams;
    }
}
