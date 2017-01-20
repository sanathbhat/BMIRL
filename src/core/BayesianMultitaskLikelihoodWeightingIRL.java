/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import misc.TrajectorySet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mdp.RewardFunction;


/**
 *
 * @author sbhat
 */
public class BayesianMultitaskLikelihoodWeightingIRL {
    //private final int K;
    HierarchicalBayesianMultitaskModel hbmm;
    private String samplesCollectionFile;
    
    /**
     * A BMIRL framework for m tasks
     * @param hbmm The underlying Hierarchical Bayesian model
     */
    public BayesianMultitaskLikelihoodWeightingIRL(HierarchicalBayesianMultitaskModel hbmm) {
        this.hbmm = hbmm;
    }
    
    /**
     * MonteCarlo sampler for BMIRL
     * @param demonstrations map of TrajectorySet(s) for each task. 
     * Key is an integer representing a task, value is the corresponding set of trajectories for that task
     * @param K Number of samples to take for the sampler
     */
    public void runMonteCarloSamplerAndWriteToFile(Map<Integer, TrajectorySet> demonstrations, int K) {
        //start sampling
        for (int k = 0; k < K; k++) {
            WeightedSample sample = hbmm.getLogWeightedSample(demonstrations);
            sample.appendSampleToFile(samplesCollectionFile);
            
            if((double)k/K*100 % 10 == 0) {
                System.out.println(k + " samples done. " + (double)k/K*100 +"% complete");
            }
        }
    }
    
    /**
     * MonteCarlo sampler for BMIRL
     * @param demonstrations map of TrajectorySet(s) for each task. 
     * Key is an integer representing a task, value is the corresponding set of trajectories for that task
     * @param K Number of samples to take for the sampler
     * @return Returns a JointRewardDistribution
     */
    public JointRewardDistribution runMonteCarloSampler(Map<Integer, TrajectorySet> demonstrations, int K) {
        //map of seen samples of reward function combinations for each of the m tasks with weights
        HashMap<List<RewardFunction>, Double> weightedSamplesMap = new HashMap<>();
        //HashMap<List<RewardFunction>, Integer> sampleCounts = new HashMap<>();
        //start sampling
        for (int k = 0; k < K; k++) {
            WeightedSample sample = hbmm.getLogWeightedSample(demonstrations);
            List<RewardFunction> rhoVector = sample.getRewardSet();
            double w = sample.getLogWeight();
            
            //if new sample, add to map
            if(!weightedSamplesMap.containsKey(rhoVector)) {
                weightedSamplesMap.put(rhoVector, w);
                //sampleCounts.put(rhoVector, 1);
            }
            else {
                weightedSamplesMap.put(rhoVector, weightedSamplesMap.get(rhoVector)+w);
                //sampleCounts.put(rhoVector, sampleCounts.get(rhoVector)+1);
            }
            
            if((double)k/K*100 % 10 == 0) {
                System.out.println(k + " samples done. " + (double)k/K*100 +"% complete");
            }
            //System.out.println("Sample "+ k + "; Weight = "+w);
        }
        
        int i=0;
        for (List<RewardFunction> rhoVector : weightedSamplesMap.keySet()) {
            System.out.println(i + "\t"+weightedSamplesMap.get(rhoVector)/* + " Count = "+sampleCounts.get(rhoVector)*/);
            i++;
        }
        //System.out.println("Total samples = " + K);
        //System.out.println("Unique samples = " + weightedSamplesMap.size());
        
        //return a joint reward distribution object representing the above map
        return new JointRewardDistribution(weightedSamplesMap, /*sampleCounts,*/ hbmm.getM());
    }

    /**
     * @param outputCollectionFile the samplesCollectionFile to set
     */
    public void setSamplesCollectionFile(String outputCollectionFile) {
        this.samplesCollectionFile = outputCollectionFile;
    }
    
}
