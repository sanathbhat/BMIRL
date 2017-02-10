/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdp;

/**
 *
 * @author sanat
 */
public class StationaryPolicy {
    int actions[];

    public StationaryPolicy(int nStates) {
        actions = new int[nStates];
    }
    
    public void setAction(int state, int action) {
        actions[state] = action;
    }
    
    public void display() {
        System.out.println("Stationary policy:");
        for (int i = 0; i < actions.length; i++) {
            System.out.println(i + "->" + actions[i]);
        }
    }
}
