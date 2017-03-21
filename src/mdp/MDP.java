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
    protected final RewardFunction R; 
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
        R = null;               //since R is null, the mdp created by this constructor will always be a CMP(Controlled Markov Process)
    }

    /**
     * Creates a new MDP with {S,A,T,gamma, epsilon} from some MDP
     * @param otherMDP
     * @param rf 
     */
    private MDP(MDP otherMDP, RewardFunction rf) {
        this.nStates = otherMDP.nStates;
        this.nActions = otherMDP.nActions;
        
        T = new double[otherMDP.T.length][][];
        for (int i = 0; i < otherMDP.T.length; i++) {
            T[i] = new double[otherMDP.T[i].length][];
            for (int j = 0; j < otherMDP.T[i].length; j++) {
                T[i][j] = new double[otherMDP.T[i][j].length];
                System.arraycopy(otherMDP.T[i][j], 0, T[i][j], 0, otherMDP.T[i][j].length);
            }
        }
        
        Q = new double[otherMDP.Q.length][];
        for (int i = 0; i < otherMDP.Q.length; i++) {
            Q[i] = new double[otherMDP.Q[i].length];
            System.arraycopy(otherMDP.Q[i], 0, Q[i], 0, otherMDP.Q[i].length);
            
        }
        
        V = new double[otherMDP.V.length];
        System.arraycopy(otherMDP.V, 0, V, 0, otherMDP.V.length);
        
        R = rf;
        
        this.gamma = otherMDP.gamma;
        this.epsilon = otherMDP.epsilon;
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
     * Method that takes in an array of rewards and creates the reward function and returns a new MDP with that reward function and other params from this MDP
     * It assumes that the first nActions# entries correspond to the rewards of the first state and so on.
     * @param rewards The array of rewards
     * @return a new MDP with the newly created reward function and other properties same as this MDP
     */ 
    public MDP setRewardFunction(double[] rewards) {
        if(rewards.length != nStates*nActions)
            throw new IllegalArgumentException("Parameter array length not appropriate for this mdp. "
                    + "Expected length="+ nStates*nActions + ", actual length="+rewards.length);
            
        double[][] rewardFunctionValues = new double[nStates][nActions];
        for (int i = 0; i < nStates; i++) {
            System.arraycopy(rewards, i*nActions, rewardFunctionValues[i], 0, nActions);
        }
        RewardFunction rf = new RewardFunction(rewardFunctionValues);
        return new MDP(this, rf);
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
//        long start = System.currentTimeMillis();
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
//        System.out.println(System.currentTimeMillis()-start);
    }
    
    public StationaryPolicy computeStationaryPolicy()  {
        StationaryPolicy policy = new StationaryPolicy(nStates);
        for (int s = 0; s < nStates; s++) {
            double maxOverActions = Double.NEGATIVE_INFINITY;
            int maximizingAction = -1;
            for (int a = 0; a < nActions; a++) {
                double sumOverSPrime = 0;
                for (int sPrime = 0; sPrime < nStates; sPrime++) {
                    sumOverSPrime += T[s][a][sPrime] * V[sPrime];
                }
                if (sumOverSPrime > maxOverActions) {
                    maxOverActions = sumOverSPrime;
                    maximizingAction = a;
                }
            }
            policy.setAction(s, maximizingAction);
        }
        return policy;
    }

    /**
     * @return the V
     */
    public double[] getV() {
        return V;
    }
    
    
}
