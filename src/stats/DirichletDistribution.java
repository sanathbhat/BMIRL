/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stats;

import org.apache.commons.math3.distribution.GammaDistribution;

/**
 *
 * @author sbhat
 */
public class DirichletDistribution extends Distribution<double[]>{
    
    /**
     * Vector of alphas of the distribution
     */
    private final double alphaVector[];
    /**
     * dimensionality
     */
    private final int k;

    public DirichletDistribution(double[] alphaVector) {
        this.k = alphaVector.length;
        this.alphaVector = new double[k];
        System.arraycopy(alphaVector, 0, this.alphaVector, 0, k);
    }

    @Override
    /**
     * Get sample using samples from Gamma distributions. Reference: https://en.wikipedia.org/wiki/Dirichlet_distribution
     */
    public double[] getSample() {
        double y[] = new double[k];
        double ySum = 0;
        for (int i = 0; i < k; i++) {
            y[i] = new GammaDistribution(alphaVector[i], 1).sample();
            ySum += y[i];
        }
        
        double x[] = new double[k];
        for (int i = 0; i < k; i++) {
            x[i] = y[i]/ySum;
        }
        return x;
    }

    @Override
    public double getProbabilityDensity(double[] x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
