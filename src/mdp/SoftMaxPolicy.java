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
public class SoftMaxPolicy implements Policy{
    public double[][] pi;

    /**
     * Creates a soft-max policy with the given Q-Function and the c parameter
     * @param q The q function to use to create the soft-max policy
     * @param c The inverse temperature parameter
     */
    public SoftMaxPolicy(double[][] q, double c) {
        pi = new double[q.length][q[0].length];
        
        for (int s = 0; s < pi.length; s++) {
            double Z = 0;
            for (int a = 0; a < pi[s].length; a++) {
                Z += Math.exp(c*q[s][a]);
            }
            Z = (Z==0)?1:Z;
            for (int a = 0; a < pi[s].length; a++) {
                pi[s][a] = Math.exp(c*q[s][a])/Z;
//                if (Double.isNaN(pi[s][a])) {
//                    System.out.println("Nan found");
//                }
            }
        }
    }
    
    @Override
    public double[] getActionsProbabilities(int state) {
        return pi[state];
    }
    
}
