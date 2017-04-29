/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mdp.StateActionPair;

/**
 *
 * @author sbhat
 */
public class TrajectoriesLoader {
    
    private final double[] trajectoryBoundaries;
    private final HashMap<Integer, TrajectorySet> allTasksTrajectories;

    public TrajectoriesLoader(double[] trajectoryBoundaries) {
        this.trajectoryBoundaries = trajectoryBoundaries;
        allTasksTrajectories = new HashMap<>();
        
        //create empty trajectory sets for each task(# tasks = # trajectory boundaries)
        for (int m = 0; m < trajectoryBoundaries.length; m++) {
            allTasksTrajectories.put(m, new TrajectorySet());
        }
    }
    
    public HashMap<Integer, TrajectorySet> loadTrajectories(String trajectoriesPath) {
        File[] files = new File(trajectoriesPath).listFiles();
        for (File file : files) {
            if(file.isDirectory())
                loadTrajectories(file.getPath());
            else {
                try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    List<StateActionPair> currentTrajectory = new ArrayList<>();
                    int m = 0;
                    int currentTaskSAPairNo = 0;
                    main: while((line = br.readLine()) != null) {
                        String lineComp[] = line.split("\t");
                        double pos = Double.parseDouble(lineComp[0]);
                        int s = Integer.parseInt(lineComp[1]);
                        int a = Integer.parseInt(lineComp[2]);

                        while(pos > trajectoryBoundaries[m]) {
                            //new task starts, add currentTrajectory to previous task trajectory set, iff it is a non-zero length trajectory
                            if(currentTrajectory.size() >0) {
                                allTasksTrajectories.get(m).add(currentTrajectory);
                            }
                            //start new trajectory for new task
                            currentTrajectory = new ArrayList<>();
                            currentTaskSAPairNo = 0;
                            m++;
                            if(m==trajectoryBoundaries.length)      //trajectories from m tasks have been read
                                break main;
                        }
//                        if(currentTaskSAPairNo%10==0)         //load every 10th (s,a) pair
                            currentTrajectory.add(new StateActionPair(s, a));   
                        
                        currentTaskSAPairNo++;
                    }
                    //if currentTrajectory still holds the last read task's trajectory, add it to its trajectory set
                    if(m<trajectoryBoundaries.length && currentTrajectory.size() >0) {
                        allTasksTrajectories.get(m).add(currentTrajectory);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }          
            }
        }
        return allTasksTrajectories;
    }
}
