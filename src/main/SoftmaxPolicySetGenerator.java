/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import mdp.CarAMDP;
import mdp.MDP;
import mdp.SoftMaxPolicy;
import misc.TrajectoriesLoader;
import misc.TrajectorySet;

/**
 *
 * @author sanat
 */
public class SoftmaxPolicySetGenerator {

    static final int NSTATES = 154;
    static final int NACTIONS = 7;
    static final int NTASKS = 3;

    public static void main(String[] args) {
        String rewardSetFilePath = "output/prunedsamples/newsample.txt";// + args[0];
        String trajectoriesPath = "data/trajectories";
        String transitionFnPath = "data/transFn2Features";
        double[] trajectoryBoundaries = new double[]{440, 840, 1200};

        double[][] rewardSet = new double[NTASKS][NSTATES * NACTIONS];
        double[] cSet = new double[NTASKS];
        double readLogWeight = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(rewardSetFilePath))) {
            String line;
            int rewardNo = 0;
            int state = 0;
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    rewardNo++;
                    state = 0;
                } else {
                    String[] currStateRewards = line.split("\t");
                    if (currStateRewards.length == 1) {
                        readLogWeight = Double.parseDouble(currStateRewards[0]);
                        continue;
                    } else if (currStateRewards.length == NTASKS) {
                        for (int i = 0; i < NTASKS; i++) {
                            cSet[i] = Double.parseDouble(currStateRewards[i]);
                        }
                        break;
                    }
                    for (int action = 0; action < currStateRewards.length; action++) {
                        rewardSet[rewardNo][state * NACTIONS + action] = Double.parseDouble(currStateRewards[action]);
                    }
                    state++;
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        CarAMDP mdp = new CarAMDP(154, 7, 0.99, 0.1);
        //mdp.loadTransitionFunction(trajectoriesPath, true);
        mdp.loadTransitionFunctionFromFile(transitionFnPath);

        double calculatedLogWeight = 0;
        HashMap<Integer, TrajectorySet> allTasksTrajectories = new TrajectoriesLoader(trajectoryBoundaries).loadTrajectories(trajectoriesPath);
        for (int i = 0; i < rewardSet.length; i++) {
            MDP newMDP = mdp.setRewardFunction(rewardSet[i]);
//            newMDP.doValueIteration();
//            newMDP.computeStationaryPolicy().display(11);
            
            /*printing softmax policy*/
            SoftMaxPolicy piI = new SoftMaxPolicy(newMDP.computeQValues(), cSet[i]);
            for (int s = 0; s < NSTATES; s++) {
                for (int a = 0; a < NACTIONS; a++) {
                    System.out.print(piI.pi(s, a) + "\t");
                }
                System.out.println();
            }
            System.out.println();
//            calculatedLogWeight += computeLogLikelihood(allTasksTrajectories.get(i), piI); 
            
        }
    }
}
