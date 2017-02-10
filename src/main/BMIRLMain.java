/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import core.BayesianMultitaskLikelihoodWeightingIRL;
import core.HierarchicalBayesianMultitaskModel;
import core.JointRewardDistribution;
import java.util.HashMap;
import mdp.CarAMDP;
import misc.TrajectoriesLoader;
import misc.TrajectorySet;

/**
 *
 * @author sbhat
 */
public class BMIRLMain {
    public static void main(String[] args) {
        String trajectoriesPath = "data/trajectories";
        String outputPath = "output/rewardDistribution.txt";
        String samplesCollectionFile = "output/samples.txt";
        
        double[] trajectoryBoundaries = new double[]{440, 840, 1200};
        int nSamples = 1000;    //1K
        //int nSamples = 10000;    //10K
        //int nSamples = 100000;    //100K
        //int nSamples = 1000000;    //1M
        //int nSamples = 5000000;    //5M
//        int nSamples = 10000000;    //10M!
        //int nSamples = 100000000;    //100M!
        
        CarAMDP mdp = new CarAMDP(154, 7, 0.99, 0.01);        
        mdp.loadTransitionFunction(trajectoriesPath, true);
        //mdp.displayTransitionFunction();
        
        HashMap<Integer, TrajectorySet> allTasksTrajectories = new TrajectoriesLoader(trajectoryBoundaries).loadTrajectories(trajectoriesPath);
        
        HierarchicalBayesianMultitaskModel mirlModel = new HierarchicalBayesianMultitaskModel(1, 1, 3, mdp);
        BayesianMultitaskLikelihoodWeightingIRL irl = new BayesianMultitaskLikelihoodWeightingIRL(mirlModel);
        irl.setSamplesCollectionFile(samplesCollectionFile);
        irl.runMonteCarloSamplerParallelAndWriteToFile(allTasksTrajectories, nSamples);
        //JointRewardDistribution jrd = irl.runMonteCarloSampler(allTasksTrajectories, nSamples);
        
        //jrd.displayDistribution();
        //jrd.writeToFile(outputPath);
    }
    
}
