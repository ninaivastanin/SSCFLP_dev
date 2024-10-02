package com.sscflp.algorithms;

import com.sscflp.common.Problem;
import com.sscflp.common.Solution;
import com.sscflp.common.SSCFLP;
import com.sscflp.common.UFLP;
import com.sscflp.neighborhood.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VNS {
    protected Problem problem;
    protected Solution solution;
    protected double optimalValue;
    protected Random rand;

    protected double gap;
    protected long executionTime;
    private static final Logger logger = LogManager.getLogger(VNS.class);

    public VNS(Problem problem) {
        this.problem = problem;
        this.solution = problem.getSolution();
        this.optimalValue = problem.getOptimalValue();
        this.rand = new Random();
    }

    public Neighborhood getNeighborhood(Solution s, int k) {
        Neighborhood neighborhood;

        problem.setSolution(s);
        neighborhood = new InvertedNeighborhood(problem, k);
/*
        switch (k) {
            case 0: neighborhood = new SwapNeighborhood(problem);
                    break;
            case 1: neighborhood = new OpenFacilityNeighborhood(problem);
                    break;
            case 2: neighborhood = new CloseFacilityNeighborhood(problem);
                    break;
            default: neighborhood = new SwapNeighborhood(problem));
                     break;
        }
 */
        return neighborhood;
    }

    // Getting the random Solution from the k-th neighborhood of the solution s
    public Solution shake(Solution s, int k) {
        List<Integer> inverted = new ArrayList<>();
        while (!s.getNeighbor(k+1, (ArrayList<Integer>) inverted)) continue;

        s = problem.calculateObjective(s);

        return s;
     /* Second solution:
        Neighborhood neighborhood = getNeighborhood(s, k);

        if (neighborhood.isEmpty())
            return s;

        return neighborhood.get(rand.nextInt(neighborhood.size()));

      */
    }

    public Solution getFirstImprovement(Solution s, int k) {
        Neighborhood neighborhood = getNeighborhood(s, k);

        if (neighborhood.isEmpty())
            return s;

        double sValue = s.getValue();

        List<Solution> neighbors = neighborhood.getNeighbors();
        for (Solution neighbor : neighbors) {
            double newValue = neighbor.getValue();
            if (newValue < sValue) {
                s = neighbor;
                sValue = newValue;
                break;
            }
        }

        return s;
    }

    public Solution getBestNeighbor(Solution s, int k) {
        Neighborhood neighborhood = getNeighborhood(s, k);

        if (neighborhood.isEmpty())
            return s;

        return neighborhood.getBestNeighbor();
    }

    public Solution algorithm(Solution solution, int kMax, int maxIterations) {
        int k, iteration;

        // Getting the start time
        long startTime = System.currentTimeMillis();

        solution = problem.calculateObjective(solution);
        double currentValue = solution.getValue();
        double bestValue = currentValue;
        iteration = 0;

        while (iteration++ < maxIterations) {
            k = 0;
            while (k < kMax) {
                Solution s = shake(solution, k);
                s = getFirstImprovement(s, k);

                //s = problem.calculateObjective(s);
                double newValue = s.getValue();


                if (!s.isAcceptable()) {
                    //logger.info("solution " + s + " isnt acceptable");
                    continue;
                }

                if (newValue < currentValue) {
                    currentValue = newValue;
                    // Restart the loop from the first neighborhood
                    k = 0;
                    solution = s;
                } else {
                    k++;
                }
                if (newValue < bestValue) {
                    bestValue = newValue;
                }
            }
        }

        // Getting the end time
        long endTime = System.currentTimeMillis();

        // Average CPU Time (milli seconds) and gap between best and optimal solution
        executionTime = endTime - startTime;
        gap = Math.abs(optimalValue - bestValue);
        //logger.info("VNS Value = " + String.format("%.3f", bestValue) + " Gap = " + String.format("%.3f", gap) + " Execution Time = " + executionTime + " Acceptable = " + solution.isAcceptable());

        solution.setValue(bestValue);
        return solution;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public double getGap() {
        return gap;
    }

    public static void main(String[] args) {
        String instancePath = "src\\main\\resources\\ORLIB-instances\\cap101";
        for (int i = 0; i < 10; i++) {
            Problem problem = new UFLP(instancePath, 796648.4);
            VNS vns = new VNS(problem);
            Solution solution = vns.algorithm(problem.getSolution(), 3, 10000);
        }
    }
}
