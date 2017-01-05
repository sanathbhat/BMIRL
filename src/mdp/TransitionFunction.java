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
class TransitionFunction<S extends State, A extends Action> {
    private final int nStates;
    private final int nActions;
    double[][][] transitionProb;
    /**
     * Denotes if the TF is consistent in the probability distribution i.e. for all s, a: \sum{sPrime} T(s, a, sPrime) = 1
     * True indicates that th condition is met. False may indicate either the condition being met or otherwise.
     */
    //boolean consistent;

    public TransitionFunction(int nS, int nA) {
        nStates = nS;
        nActions = nA;
        transitionProb = new double[nStates][nActions][nS];
        //consistent = false;
    }
    
    /**
     * Gets the probability of reaching state sPrime from state s if action a is performed
     * @param s
     * @param a
     * @param sPrime
     * @return 
     */    
    public double T(S s, A a, S sPrime) {
        return transitionProb[s.getStateId()][a.getActionId()][sPrime.getStateId()];
    }
    
    private void setProbability(S s, A a, S sPrime, double prob) {
        transitionProb[s.getStateId()][a.getActionId()][sPrime.getStateId()] = prob;
    }
    
    public void makeConsistent() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
