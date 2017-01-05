/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdp;

/**
 *
 * @author sbhat
 */
public class QFunction {
    private final int nStates;
    private final int nActions;
    private final double[][] rewardFunction;

    public QFunction() {
        this.nStates = 0;
        this.nActions = 0;
        this.rewardFunction = null;
    }
    
    public double Q(int s, int a) {
        return rewardFunction[s][a];
    }

    /**
     * @return the nStates
     */
    public int getnStates() {
        return nStates;
    }

    /**
     * @return the nActions
     */
    public int getnActions() {
        return nActions;
    }
}
