package com.sscflp.algorithms;


import com.sscflp.common.Problem;
import com.sscflp.common.Solution;
import com.sscflp.common.SSCFLP;
import com.sscflp.common.UFLP;
import com.sscflp.neighborhood.Neighborhood;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VND extends VNS {

    private static final Logger logger = LogManager.getLogger(VND.class);

    public VND(Problem problem) {
        super(problem);
    }

    public Solution algorithm(Solution solution, int kMax) {
        // Getting the start time
        long startTime = System.currentTimeMillis();

        int k = 0;
        solution = problem.calculateObjective(solution);
        double currentValue = solution.getValue();

        while (k < kMax) {
            Neighborhood neighborhood = getNeighborhood(solution, k);
            if (neighborhood.isEmpty()) {
                k++;
                continue;
            }

            // Find the best solution in k-th neighborhood
            Solution s1 = neighborhood.getBestNeighbor();
            s1 = problem.calculateObjective(s1);
            double s1Value = s1.getValue();
            if (s1Value < currentValue) {
                // Restart the loop from the first neighborhood
                k = 0;
                currentValue = s1Value;
                solution = s1;
            } else k++;
        }

        // Getting the end time
        long endTime = System.currentTimeMillis();

        // Average CPU Time (milli seconds) and gap between best and optimal solution
        executionTime = endTime - startTime;
        gap = Math.abs(optimalValue - solution.getValue());
        //logger.info("VND Value = " +  String.format("%.3f", solution.getValue()) + " Gap = " + String.format("%.3f", gap) + " Execution Time = " + executionTime + " Acceptable = " + solution.isAcceptable());

        return solution;
    }

    public static void main(String[] args) {
        String instancePath = "src\\main\\resources\\ORLIB-instances\\cap71";
        for (int i = 0; i < 10; i++) {
            Problem problem = new SSCFLP(instancePath, 932615.7);
            VND vnd = new VND(problem);
            Solution solution = vnd.algorithm(problem.getSolution(), 3, 1000);
        }
    }
}
