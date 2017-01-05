/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stats;

/**
 *
 * @author sbhat
 */
public class ExponentialDistribution extends Distribution<Double>{
    
    private final double lambda;
    /**
     *
     * @param lambda the lambda value
     */
    public ExponentialDistribution(double lambda) {
        this.lambda = lambda;
    }

    @Override
    public Double getSample() {
        double u = randomizer.nextDouble();
        return -Math.log(1-u)/lambda;   //theoretically -Math.log(u)/lambda works, but practically the randomizer generates value 0 and never generates value 1, so 1-u is suitable
    }

    @Override
    public double getProbabilityDensity(Double x) {
        return lambda * Math.exp(-lambda*x);
    }
    
}
