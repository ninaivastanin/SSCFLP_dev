package com.sscflp.algorithms;

import com.sscflp.common.Problem;
import com.sscflp.common.Solution;
import com.sscflp.common.SSCFLP;
import com.sscflp.common.UFLP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Reduced variable neighborhood search algorithm
public class ReducedVNS extends VNS {
    private static final Logger logger = LogManager.getLogger(ReducedVNS.class);

    public ReducedVNS(Problem problem) {
        super(problem);
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
                // Getting the random Solution from the k-th neighborhood
                Solution s = shake(solution, k);

                //s = problem.calculateObjective(s);
                if (!s.isAcceptable()) {
                    //logger.info("solution " + s + " isnt acceptable");
                    continue;
                }
                double newValue = s.getValue();

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
        //logger.info("ReducedVNS Value = " + String.format("%.3f", bestValue) + " Gap = " + String.format("%.3f", gap) + " Execution Time = " + executionTime + " Acceptable = " + solution.isAcceptable());

        solution.setValue(bestValue);
        return solution;
    }

    public static void main(String[] args) {
        String instancePath = "src\\main\\resources\\ORLIB-instances\\cap71";
        for (int i = 0; i < 10; i++) {
            Problem problem = new SSCFLP(instancePath, 932615.7);
            ReducedVNS reducedVNS = new ReducedVNS(problem);
            Solution solution = reducedVNS.algorithm(problem.getSolution(),3, 10000);
        }
    }
}
