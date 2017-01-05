/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdp;

/**
 *
 * @author sbhat
 */
public abstract class State<T>{
    private static int stateIdCounter = 0;
    private final int stateId;
    
    public State() {
        this.stateId = stateIdCounter++;
    }

    @Override
    public boolean equals(Object obj) {
        //same reference check
        if (this == obj)
            return true;
        //parameter null check and parameter type check
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        
        return this.stateId == ((State)obj).stateId; 
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.stateId;
        return hash;
    }

    /**
     * @return the stateId
     */
    public int getStateId() {
        return stateId;
    }

    @Override
    public String toString() {
        return "s"+stateId; //To change body of generated methods, choose Tools | Templates.
    }
    
}
