/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stats;

import java.util.Arrays;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author sanat
 */
public class BiasedRewardPrior extends Distribution<double[]> {
    
    private final int nStates;
    private final int rci;  //reward cycle interval
    private final int nActions;
    private final double alpha;     //peakiness prior
    private final double stateMeanNormalizer;       //the divisor for state mean to bring it into the appropriate [0,6] interval

    public BiasedRewardPrior(int nStates, int nActions, double alpha, int rci, double stateMeanNormalizer) {
        this.nStates = nStates;
        this.nActions = nActions;
        this.alpha = alpha;
        this.rci = rci;
        this.stateMeanNormalizer = stateMeanNormalizer;
    }

    @Override
    public double[] getSample() {
        UniformDistribution uMeanShiftPrior = new UniformDistribution(0, alpha);
        double m = uMeanShiftPrior.getSample();      //controls mean-shift of reward curve
        
        UniformDistribution uVarPrior = new UniformDistribution(0, alpha);
        double v = uVarPrior.getSample();       //controls 'peakiness' of reward curve. 
        
        ExponentialDistribution rVarPrior = new ExponentialDistribution(200);
        
        double rewardFunction[][] = new double[nStates][nActions];
        for (int s = 0; s < nStates; s++) {
            double ms = (m + (s/rci + s%rci)/stateMeanNormalizer) % nActions;
            //ms = (ms > (nActions-1)? ms-nActions: ms);
//            double ms = (s/11 + s%11)/3.3;
            for (int a = 0; a < nActions; a++) {
                double rMean = Math.exp((-(a-ms)*(a-ms))/(2*v)) / Math.sqrt(2*Math.PI*v);
                double rVar = rVarPrior.getSample();
                rewardFunction[s][a] = new NormalDistribution(rMean, Math.sqrt(rVar)).sample();
            }
        }
        
        double rewardValues[] = new double[nStates*nActions];
        for (int s = 0; s < rewardFunction.length; s++) {
            double currSMinRew = Arrays.stream(rewardFunction[s]).min().getAsDouble();
                double translator = Math.abs(currSMinRew);
                for(int a = 0; a < rewardFunction[s].length; a++) {
                    rewardFunction[s][a] += translator;
                    rewardValues[nActions*s + a] = ((int)(rewardFunction[s][a]*1000))/1000.0;
                }
        }
        
        return rewardValues;
    }

    @Override
    public double getProbabilityDensity(double[] x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
