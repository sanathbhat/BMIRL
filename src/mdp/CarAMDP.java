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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sbhat
 */
public class CarAMDP extends MDP{
    
    public CarAMDP(int nStates, int nActions, double gamma, double epsilon) {
        super(nStates, nActions, gamma, epsilon);
    }
    
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
