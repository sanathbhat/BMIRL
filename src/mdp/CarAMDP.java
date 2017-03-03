/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * @author sbhat
 */
public class CarAMDP extends MDP{
    static final double spacingIntervals[] = {0, 14, 28, 42, 56, 70, 84, 98, 112, 126, 140, 168, 196};
    static final double relativeVelocityIntervals[] = {-45, -35, -25, -15, -5, 5, 15, 25, 35, 45};
    static final double actionIntervals[] = {-11.2, -9, -4.8, -0.6, 0.6, 4.8, 9, 11.2};
    static final int TRANSITION_SAMPLES = 100;
    
    public CarAMDP(int nStates, int nActions, double gamma, double epsilon) {
        super(nStates, nActions, gamma, epsilon);
    }
    
    public void loadTransitionFunctionFromFile(String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int s = 0;
            while((line = br.readLine()) != null) {
                String sCurr[] = line.split("\t");
                for (int a = 0; a < getnActions(); a++) {
                    String saCurr[] = sCurr[a].split(",");
                    for (int sPrime = 0; sPrime < getnStates(); sPrime++) {
                        T[s][a][sPrime] = Double.parseDouble(saCurr[sPrime]);
                    }
                }
                s++;
            }                    
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Loading transition function completed");
    }
    
//    public void loadTransitionFunction(String trajectoriesPath, boolean isTopLevelDirectory) {
//        File[] files = new File(trajectoriesPath).listFiles();
//        for (File file : files) {
//            if(file.isDirectory())
//                loadTransitionFunction(file.getPath(), false);
//            else {
//                try(BufferedReader br = new BufferedReader(new FileReader(file))) {
//                    String line;
//                    //variables to store the last read state and action so that when the corresponding sPrime(state in the next line) is read, the T function can be updated
//                    int prevS = -99;
//                    int prevA = -99;
//                    while((line = br.readLine()) != null) {
//                        String lineComp[] = line.split("\t");
//                        int s = Integer.parseInt(lineComp[1]);
//                        int a = Integer.parseInt(lineComp[2]);
//                        
//                        //update T if valid (s, a, sPrime) triple
//                        if (prevS>=0 && prevA>=0 && s>=0) {
//                            for (int i = 0; i < getnActions(); i++) {
//                                for (int n = 0; n < TRANSITION_SAMPLES; n++) {
//                                    
//                                }
//                            }
//                        }
//                        prevS = s;
//                        prevA = a;
//                    }                    
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }                
//            }
//        }
//        
//        if (isTopLevelDirectory) {
//            normalizeTransitionFunction();
//            System.out.println("Loading transition function completed");
//        }        
//    }
    
//    private int getState(double spacing, double relVelocity) {
//        int nSpacingIntervals = spacingIntervals.length;
//        int spacingInterval;
//        for (spacingInterval = 0; spacingInterval < nSpacingIntervals; spacingInterval++) {
//            if (spacing < spacingIntervals[spacingInterval]) {
//                break;
//            }
//        }
//
//        int nRelativeVelocityIntervals = relativeVelocityIntervals.length;
//        int relVeloInterval;
//        for (relVeloInterval = 0; relVeloInterval < nRelativeVelocityIntervals; relVeloInterval++) {
//            if (relVelocity < relativeVelocityIntervals[relVeloInterval]) {
//                break;
//            }
//        }
//
//        return spacingInterval*(relativeVelocityIntervals.length + 1) + relVeloInterval;
//    }
    
//    private double getXac(int state) {
//        int stateInterval = state/(relativeVelocityIntervals.length+1);
//        
//    }
//    
//    private double getVac(int state) {
//        int relVelInterval = state%(relativeVelocityIntervals.length+1);
//    }
    
    public void loadTransitionFunction(String trajectoriesPath, boolean isTopLevelDirectory) {
        File[] files = new File(trajectoriesPath).listFiles();
        for (File file : files) {
            if(file.isDirectory())
                loadTransitionFunction(file.getPath(), false);
            else {
                try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    //variables to store the last read state and action so that when the corresponding sPrime(state in the next line) is read, the T function can be updated
                    int prevS = -99;
                    int prevA = -99;
                    while((line = br.readLine()) != null) {
                        String lineComp[] = line.split("\t");
                        int s = Integer.parseInt(lineComp[1]);
                        int a = Integer.parseInt(lineComp[2]);
                        
                        //update T if valid (s, a, sPrime) triple
                        if (prevS>=0 && prevA>=0 && s>=0) {
                            T[prevS][prevA][s] += 1;
                        }
                        prevS = s;
                        prevA = a;
                    }                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }                
            }
        }
        
        if (isTopLevelDirectory) {
            normalizeTransitionFunction();
            System.out.println("Loading transition function completed");
        }        
    } 
}
