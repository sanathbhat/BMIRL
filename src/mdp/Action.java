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
public abstract class Action {
    private static int actionIdCounter = 0;
    private final int actionId;

    public Action() {
        this.actionId = actionIdCounter++;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        return this.actionId == ((Action)obj).actionId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.actionId;
        return hash;
    }

    /**
     * @return the actionId
     */
    public int getActionId() {
        return actionId;
    }

    @Override
    public String toString() {
        return "a"+actionId; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
