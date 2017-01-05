/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stats;

import java.util.Random;

/**
 *
 * @author sbhat
 */
public abstract class Distribution<T> {
    
    protected Random randomizer;
    
    public Distribution() {
        randomizer = new Random();
    }
    
    /**
     * Get a sample for the distribution using Inverse transform sampling
     * @return
     */
    public abstract T getSample();

    /**
     * Get the value of the pdf of the distribution at x
     * @param x
     * @return
     */
    public abstract double getProbabilityDensity(T x);
}
