/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stats;

/**
 *
 * @author sanat
 */
public class UniformDistribution extends Distribution<Double>{

    private final double min;
    private final double max;

    public UniformDistribution(double min, double max) {
        this.min = min;
        this.max = max;
    }
    
    @Override
    public Double getSample() {
        return randomizer.nextDouble()*(max-min) + min;
    }

    @Override
    public double getProbabilityDensity(Double x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
