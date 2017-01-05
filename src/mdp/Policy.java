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
public interface Policy {
    /**
     * returns action probability distribution for given state
     * @param state
     * @return 
     */
    public double[] getActionsProbabilities(int state);
    
    /**
     * Returns the probability \pi(a|s) i.e. probability of action a for the given state s
     * @param a
     * @param s
     * @return 
     */
    public default double pi(int s, int a) {
        return getActionsProbabilities(s)[a];
    }
}
