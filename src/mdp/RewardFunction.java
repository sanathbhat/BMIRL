/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdp;

import java.util.Arrays;

/**
 * Standard R(s,a) type reward function
 * @author sbhat
 */
public class RewardFunction {
    private final int nStates;
    private final int nActions;
    private final double[][] rewardValues;

    /**
     * Constructor that takes in 2D array of rewards, first index represents state, second represents action
     * @param rewards 
     */
    public RewardFunction(double[][] rewards) {
        nStates = rewards.length;
        nActions = rewards[0].length;    
        rewardValues = new double[nStates][nActions];
        for (int i = 0; i < nStates; i++) {
            System.arraycopy(rewards[i], 0, rewardValues[i], 0, nActions);
        }
    }
    
    public double R(int s, int a) {
        return rewardValues[s][a];
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        
        RewardFunction other = (RewardFunction)obj;
        if (rewardValues.length != other.rewardValues.length || rewardValues[0].length != other.rewardValues[0].length) {
            return false;
        }
        
        for (int i = 0; i < rewardValues.length; i++) {
            if(!Arrays.equals(rewardValues[i], other.rewardValues[i]))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.nStates;
        hash = 59 * hash + this.nActions;
        hash = 59 * hash + Arrays.deepHashCode(this.rewardValues);
        return hash;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < nStates; i++) {
            for (int j = 0; j < nActions; j++) {
                s += (rewardValues[i][j] + ",");
            }
        }
        return s;
    }
        
}
