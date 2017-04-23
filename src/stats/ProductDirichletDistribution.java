/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stats;

import org.apache.commons.math3.distribution.GammaDistribution;

/**
 *
 * @author sanat
 */
public class ProductDirichletDistribution extends Distribution<double[]> {

    /**
     * The set of alpha vectors for this product Dirichlet distribution
     */
    private final double alphaVectors[][];
    /**
     * No of Dirichlet distribution components in the product
     */
    private final int n;
    /**
     * Dimensionality of each component
     */
    private final int k;

    public ProductDirichletDistribution(double[] alphaVectors, int n, int k) {
        this.n = n;
        this.k = k;
        this.alphaVectors = new double[n][k];
        for (int i = 0; i < n; i++) {
            System.arraycopy(alphaVectors, i*k, this.alphaVectors[i], 0, k);
        }
    }

    @Override
    public double[] getSample() {
        double x[] = new double[n*k];
        for (int i = 0; i < n; i++) {
            double y[] = new double[k];
            double ySum = 0;
            for (int j = 0; j < k; j++) {
                y[j] = new GammaDistribution(alphaVectors[i][j], 1).sample();
                ySum += y[j];
            }

            double xTemp[] = new double[k];
            for (int j = 0; j < k; j++) {
                xTemp[j] = y[j] / ySum;
            }
            System.arraycopy(xTemp, 0, x, i*k, k);
        }
        return x;
    }

    @Override
    public double getProbabilityDensity(double[] x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
