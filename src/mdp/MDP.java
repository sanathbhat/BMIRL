/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdp;

import java.text.DecimalFormat;

/**
 *
 * @author sbhat
 */
public class MDP {
    static final boolean LOGGER_ON = false;
    private final int nStates;
    private final int nActions;
    /**
     * The transition function of this MDP
     */
    protected final double[][][] T;
    private final double[][] Q;
    private final double[] V;
    /**
     * The reward function of this MDP
     */
    protected RewardFunction R; 
    private final double gamma;
    /**
     * Error in value iteration
     */
    private final double epsilon;

    public MDP(int nStates, int nActions, double gamma, double epsilon) {
        this.nStates = nStates;
        this.nActions = nActions;
        T = new double[nStates][nActions][nStates];
        Q = new double[nStates][nActions];
        V = new double[nStates];
        this.gamma = gamma;
        this.epsilon = epsilon;
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
    
    /**
     * Method that takes in an array of transition probabilities and creates the transition function
     * It assumes that the first nState# entries correspond to the transitions of the first state and first action,
     *                 the next nState# entries correspond to the transitions of the first state and second action and so on.
     * @param transitionProbs
     */ 
    public void setTransitionFunction(double[] transitionProbs) {
        if(transitionProbs.length != nStates*nActions*nStates)
            throw new IllegalArgumentException("Parameter array length not appropriate for this mdp. "
                    + "Expected length="+ nStates*nActions*nStates + ", actual length="+transitionProbs.length);
            
        for (int i = 0; i < nStates; i++) {
            for (int j = 0; j < nActions; j++) {
                System.arraycopy(transitionProbs, (i*nActions+j)*nStates, T[i][j], 0, nStates);
            }            
        }
    }
    
    protected void normalizeTransitionFunction() {
        for (double[][] Ts : T) {
            for (double[] Tsa : Ts) {
                double sum = 0;
                for (int sPrime = 0; sPrime < Tsa.length; sPrime++) {
                    sum += Tsa[sPrime];
                }
                sum = sum==0?1:sum;
                for (int sPrime = 0; sPrime < Tsa.length; sPrime++) {
                    Tsa[sPrime] /= sum;
                }
            }
        }
    }
    
    public void normalizeRewardFunction(double precision) {
        R.normalize(precision);
    }
    
    public void displayTransitionFunction() {
        DecimalFormat df = new DecimalFormat("0.000");
        System.out.println("(s,a) -> distribution over sPrime");
        for (int s = 0; s < T.length; s++) {
            for (int a = 0; a < T[s].length; a++) {
                System.out.print(s + "," + a + "-> ");
                for (int sPrime = 0; sPrime < T[s][a].length; sPrime++) {
                    System.out.print(df.format(T[s][a][sPrime]) + "\t");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /**
     * Method that takes in an array of rewards and creates the reward function
     * It assumes that the first nActions# entries correspond to the rewards of the first state and so on.
     * @param rewards The array of rewards
     */ 
    public void setRewardFunction(double[] rewards) {
        if(rewards.length != nStates*nActions)
            throw new IllegalArgumentException("Parameter array length not appropriate for this mdp. "
                    + "Expected length="+ nStates*nActions + ", actual length="+rewards.length);
            
        double[][] rewardFunctionValues = new double[nStates][nActions];
        for (int i = 0; i < nStates; i++) {
            System.arraycopy(rewards, i*nActions, rewardFunctionValues[i], 0, nActions);
        }
        R = new RewardFunction(rewardFunctionValues);
    }

    /**
     * Computes and returns Q values of this MDP
     * @return double array of Q(s,a)
     */
    public double[][] computeQValues() {
        doValueIteration();
        for (int s = 0; s < nStates; s++) {
            for (int a = 0; a < nActions; a++) {
                double sumOverSPrime = 0;
                for (int sPrime = 0; sPrime < nStates; sPrime++) {
//                    double maxOverAPrime = Double.NEGATIVE_INFINITY;
//                    for (int aPrime = 0; aPrime < nActions; aPrime++) {
//                        if(Q[sPrime][aPrime]>maxOverAPrime)
//                            maxOverAPrime = Q[sPrime][aPrime];
//                    }
//                    sumOverSPrime += T[s][a][sPrime]*maxOverAPrime;
                      sumOverSPrime += T[s][a][sPrime]*V[sPrime];
                }                
                Q[s][a] = R.R(s, a) + gamma*sumOverSPrime;
                if(Double.isNaN(Q[s][a]))
                    System.out.println("Nan found");
            }
        }
        return Q;
    }

    /**
     * @return the R
     */
    public RewardFunction getRewardFunction() {
        return R;
    }
    
    public void doValueIteration(){
        if(R==null)
            throw new IllegalStateException("Reward function not set. Cannot compute Q");
        
        double[] VPrime = new double[nStates];
        double delta;
        int iterations = 0;
        do {
            delta = 0;
            System.arraycopy(VPrime, 0, V, 0, nStates); //V <- VPrime
            for (int s = 0; s < nStates; s++) {
                //compute new value of state
                double maxValueOverA = Double.NEGATIVE_INFINITY;
                for (int a = 0; a < nActions; a++) {
                    double sumOverSPrime = 0;
                    for (int sPrime = 0; sPrime < nStates; sPrime++) {
                        sumOverSPrime += T[s][a][sPrime]*V[sPrime];
                    }
                    double currentActionValue = R.R(s, a) + gamma*sumOverSPrime;
                    if(currentActionValue > maxValueOverA)
                        maxValueOverA = currentActionValue;
                }                
                VPrime[s] = maxValueOverA;
                
                //update delta if required
                double currentStateValueChange = Math.abs(VPrime[s] - V[s]);
                if(currentStateValueChange > delta)
                    delta = currentStateValueChange;                
            }
            iterations++;
            if(LOGGER_ON)
                System.out.println("Iterations completed="+iterations + "\tDelta = "+ delta);
        }while(delta > epsilon*(1-gamma)/gamma);
    }

    /**
     * @return the V
     */
    public double[] getV() {
        return V;
    }
    
    
}
