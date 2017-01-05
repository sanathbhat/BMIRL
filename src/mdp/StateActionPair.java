/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdp;

public class StateActionPair {
    private final int s;
    private final int a;

    public StateActionPair(int s, int a) {
        this.s = s;
        this.a = a;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
	if(obj == null || obj.getClass() != this.getClass())
            return false;
        
        StateActionPair paramObj = (StateActionPair)obj;
        return s == paramObj.s && a == paramObj.a;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.s;
        hash = 79 * hash + this.a;
        return hash;
    }
    
    public int getS(){
        return s;
    }
    
    public int getA(){
        return a;
    }
    
}



/*
//import java.util.Objects;

/**
 *
 * @author sbhat
 * @param <S>
 * @param <A>

public class StateActionPair<S extends State, A extends Action> {
    private final S s;
    private final A a;

    public StateActionPair(S s, A a) {
        this.s = s;
        this.a = a;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
	if(obj == null || obj.getClass() != this.getClass())
            return false;
        
        StateActionPair paramObj = (StateActionPair)obj;
        return s.equals(paramObj.s) && a.equals(paramObj.a);
    }

    @Override
    public int hashCode() {
        return Objects.hash(s.getStateId(), a.getActionId());
    }  
    
}
*/