/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stats;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author sanat
 */
public class GaussianDistribution extends Distribution<Double>{

    private final double mean;
    private final double variance;

    public GaussianDistribution(double mean, double variance) {
        this.mean = mean;
        this.variance = variance;
    }
    
    @Override
    public Double getSample() {
        return new NormalDistribution(mean, Math.sqrt(variance)).sample();
    }

    @Override
    public double getProbabilityDensity(Double x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
