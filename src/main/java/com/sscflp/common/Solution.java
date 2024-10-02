package com.sscflp.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Solution {
    private static final int INIT_PROBABILITY = 25;

    private int nbResources;
    private List<Boolean> solutionArrayList;
    private Map<Integer,ArrayList<Integer>> solutionDetailsMap;
    private double value;
    private boolean acceptable;
    private Random rand;
    private static final Logger logger = LogManager.getLogger(Solution.class);

    public Solution(int nbResources){
        this.nbResources = nbResources;
        this.rand = new Random();
        this.acceptable = false;
        initialize();
    }

    public Solution(int nbResources, List<Boolean> solutionArrayList){
        this.nbResources = nbResources;
        this.rand = new Random();
        this.acceptable = false;
        this.solutionArrayList = solutionArrayList;
    }

    // Initialization of the solution array list (INIT_PROBABILITY is the probability of resource establishment)
    public void initialize() {
        solutionArrayList = new ArrayList<>(nbResources);
        for (int j = 0; j < nbResources; j++) {
            solutionArrayList.add(rand.nextInt(100) < INIT_PROBABILITY);
        }

        if (!hasEstablishedResources()) {
            int j = rand.nextInt(nbResources);
            solutionArrayList.set(j, true);
        }

        //logger.info("Initial solutionArrayList: " + solutionArrayList.toString());
    }

    // Function that returns true if at least one resource is established
    public boolean hasEstablishedResources() {
        for (boolean value : solutionArrayList) {
            if (value) return true;
        }
        return false;
    }

    public boolean hasEstablishedResources(ArrayList<Boolean> solutionList) {
        for (boolean value : solutionList) {
            if (value) return true;
        }
        return false;
    }

    public int openResource() {
        int j;
        // Find unestablished resource index
        j = rand.nextInt(nbResources);
        while (solutionArrayList.get(j)) {
            j = rand.nextInt(nbResources);
        }
        solutionArrayList.set(j, true);
        return j;
    }

    // A function that inverts a random element in the solution array list
    // If after inversion the solution list has at least one established resource, it returns the inverted index
    // Otherwise, it cancels the inversion and returns the -1
    public int invert() {
        int j = rand.nextInt(nbResources);
        solutionArrayList.set(j, !solutionArrayList.get(j));
        if (hasEstablishedResources()) return j;
        solutionArrayList.set(j, !solutionArrayList.get(j));
        return -1;
    }

    // A function that inverts j-th element of the solution array list
    public void restore(int j) {
        solutionArrayList.set(j, !solutionArrayList.get(j));
    }

    // A function that inverts the elements of the solution list up to inverted.size position
    public void restore(ArrayList<Integer> inverted) {
        int size = inverted.size();
        for (int j = 0; j < size; j++) {
            solutionArrayList.set(j, !solutionArrayList.get(j));
        }
    }

    // Function for getting the neighbors in the k-th neighborhood
    // it is obtained by inverting k positions in the solutionArrayList
    public boolean getNeighbor(int k, ArrayList<Integer> inverted) {
        inverted.clear();
        for (int i = 0; i < k; i++) {
            int j = rand.nextInt(nbResources);
            // Find the index in the solution array list that is not inverted
            while (inverted.contains(j)) {
                j = rand.nextInt(nbResources);
            }
            // Add an index to the inverted list and invert the element at that position in solution list
            inverted.add(j);
            solutionArrayList.set(j, !solutionArrayList.get(j));
        }

        if (hasEstablishedResources()) {
            // If we found a neighbor whose solution has at least one established resource, function returns true
            return true;
        } else {
            // If the neighbor doesn't have at least one established resource, function restores the inverted elements
            //  of the solution list to their old values and returns false
            //logger.info("getNeighbor, nema uspostavljenih resursa, vraca orginalni Solution");
            restore(inverted);
            return false;
        }
    }

    public List<Boolean> getSolutionArrayList() {
        return solutionArrayList;
    }

    public void setSolutionArrayList(List<Boolean> solutionArrayList) {
        this.solutionArrayList = solutionArrayList;
    }

    public boolean getSolutionArrayListElement(int index) {
        return (Boolean) solutionArrayList.get(index);
    }
    public void setSolutionArrayListElement(int index, boolean value) {
        solutionArrayList.set(index, value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isAcceptable() {
        return acceptable;
    }

    public void setAcceptable(boolean acceptable) {
        this.acceptable = acceptable;
    }

    public void setSolution(ArrayList<Boolean> solutionArrayList, double value, boolean acceptable) {
        this.solutionArrayList = solutionArrayList;
        this.value = value;
        this.acceptable = acceptable;
    }

    public Map<Integer, ArrayList<Integer>> getSolutionDetailsMap() {
        return solutionDetailsMap;
    }

    public void setSolutionDetailsMap(Map<Integer, ArrayList<Integer>> solutionDetailsMap) {
        this.solutionDetailsMap = solutionDetailsMap;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "solutionArrayList=" + solutionArrayList.toString() +
                ", value=" + value +
                ", acceptable=" + acceptable +
                '}';
    }
}
