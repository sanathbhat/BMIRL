/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author sanat
 */
public class SamplesProcessorMain {
    public static void main(String[] args) {
        String samplesPath = "data/samples.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(samplesPath))) {
            String line;
            while((line = br.readLine()) != null) {
                
            }
        } catch (Exception e) {
        }
    }
}
