/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mdp.StateActionPair;

/**
 * Class representing a set of trajectories 
 * @author sbhat
 */
public class TrajectorySet implements Iterable<List<StateActionPair>>{
    private final List<List<StateActionPair>> trajectories;

    /**
     * Creates an empty trajectory set
     */
    public TrajectorySet() {
        trajectories = new ArrayList<>();
    }

    /**
     * Adds a trajectory(List<StateActionPair>) to this trajectory set
     * @param trajectory 
     */
    public void add(List<StateActionPair> trajectory) {
        trajectories.add(trajectory);
    }
    
    /**
     * Returns the i'th trajectory of this set
     * @param i
     * @return 
     */
    public List<StateActionPair> get(int i) {
        return trajectories.get(i);
    }
    
    public int size() {
        return trajectories.size();
    }

    @Override
    public Iterator<List<StateActionPair>> iterator() {
        return trajectories.iterator();
    }
    
}
