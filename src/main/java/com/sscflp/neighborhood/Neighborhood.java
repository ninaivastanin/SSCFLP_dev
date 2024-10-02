package com.sscflp.neighborhood;

import com.sscflp.common.Solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Neighborhood {

    private List<Solution> neighbors;
    private Solution bestNeighbor;

    public Neighborhood() {
        neighbors = new ArrayList<Solution>();
    }

    public boolean add(Solution solution) {
        return neighbors.add(solution);
    }

    public Solution get(int i) {
        return neighbors.get(i);
    }

    public boolean set(ArrayList<Solution> neighborhood) {
        neighbors.clear();
        return neighbors.addAll(neighborhood);
    }

    public List<Solution> getNeighbors() {
        return neighbors;
    }

    public void clear() {
        neighbors.clear();
    }

    public boolean isEmpty() {
        return neighbors.isEmpty();
    }

    public int size() {
        return neighbors.size();
    }

    public Solution getBestNeighbor() {
        return bestNeighbor;
    }

    public void setBestNeighbor() {
        this.bestNeighbor = (!neighbors.isEmpty()) ? neighbors.stream().min(Comparator.comparing(Solution::getValue)).get() : null;
    }

    @Override
    public String toString() {
        return "Neighborhood { " +
                "neighbors = " + neighbors.toString() +
                '}';
    }
}
