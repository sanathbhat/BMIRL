/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.List;
import java.util.Map;
import mdp.MDP;
import mdp.SoftMaxPolicy;
import mdp.StateActionPair;
import misc.TrajectorySet;
import stats.DirichletDistribution;
import stats.Distribution;
import stats.ExponentialDistribution;

/**
 *
 * @author sbhat
 */
public class HierarchicalBayesianMultitaskModel {
    
    private final Distribution<Double> rewardHyperprior;
    private final Distribution<Double> softmaxHyperprior;
    private final int m;  //number of tasks
    private final MDP mdp;
    

    /**
     *
     * @param rewardEta The hyperparameter of the reward hyperprior
     * @param policyEta The hyperparameter of the soft-max hyperprior
     * @param m The number of tasks to model
     * @param mdp The mdp model for the process
     */
    public HierarchicalBayesianMultitaskModel(double rewardEta, double policyEta, int m, MDP mdp) {
        rewardHyperprior = new ExponentialDistribution(rewardEta);
        softmaxHyperprior = new ExponentialDistribution(policyEta);
        this.m = m;
        this.mdp = mdp;
    }

    WeightedSample getLogWeightedSample(Map<Integer, TrajectorySet> demonstrations) {
        //sample beta i.e. the parameter of the Exponential softmax prior over the policy's c parameter
        double beta = softmaxHyperprior.getSample();    //c prior parameter
        ExponentialDistribution exponentialCPrior = new ExponentialDistribution(beta);
        
        //sample alphaVector i.e. the parameter set for the Dirichlet reward prior
        double[] alpha = new double[mdp.getnStates()*mdp.getnActions()];    //reward prior parameters
        for (int i = 0; i < alpha.length; i++) {
            alpha[i] = rewardHyperprior.getSample();
        }
        DirichletDistribution dirichletRewardPrior = new DirichletDistribution(alpha);
        
        WeightedSample wrs = new WeightedSample();
        wrs.setBeta(beta);
        wrs.setAlpha(alpha);
        //sample m rewards and policies and compute log likelihood weight
        double logWeight = 0;
        for (int i = 0; i < m; i++) {
            //sample c parameter for softmax policy
            double cI = exponentialCPrior.getSample();
            wrs.addCParam(cI);
            //sample a reward function and complete the mdp so that Q values can be calculated
            mdp.setRewardFunction(dirichletRewardPrior.getSample());
            SoftMaxPolicy piI = new SoftMaxPolicy(mdp.computeQValues(), cI);
            logWeight += computeLogLikelihood(demonstrations.get(i), piI, cI);
//            if(Double.isNaN(logWeight))
//                System.out.println("NaN found!");
            wrs.addReward(mdp.getRewardFunction());
        }
        wrs.setLogWeight(logWeight);
//        if(Double.isNaN(logWeight))
//            System.out.println("NaN found!");
//        else
//            System.out.println("Weight="+logWeight);
        return wrs;
    }
    
    private double computeLogLikelihood(TrajectorySet tSet, SoftMaxPolicy policy, double c) {
        double logW = 0;
        for (List<StateActionPair> trajectory : tSet) {
            for (StateActionPair sa : trajectory) {
                logW += Math.log(policy.pi(sa.getS(), sa.getA()));
            }
        }
        return logW;
    }

    /**
     * @return the m
     */
    public int getM() {
        return m;
    }
    
}
