package com.sscflp.algorithms;

import com.sscflp.common.Problem;
import com.sscflp.common.SSCFLP;
import com.sscflp.common.Solution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// General variable neighborhood search algorithm
public class GeneralVNS extends VNS {
    private ReducedVNS reducedVNS;
    private VND vnd;
    private static final Logger logger = LogManager.getLogger(GeneralVNS.class);

    public GeneralVNS(Problem problem) {
        super(problem);
        this.reducedVNS = new ReducedVNS(this.problem);
        this.vnd = new VND(this.problem);
    }

    public Solution algorithm(Solution solution, int kMax, int lMax, int maxIterations) {
        int k, iteration;

        // Getting the start time
        long startTime = System.currentTimeMillis();

        // Improving the initial solution using the reduced variable neighborhood search algorithm (s)
        // and setting the value of the best solution
        Solution s =  reducedVNS.algorithm(solution, kMax, maxIterations);
        double currentValue = s.getValue();
        double bestValue = currentValue;

        iteration = 0;
        while (iteration++ < maxIterations) {
            k = 0;
            while (k < kMax) {
                Solution s1 = shake(s, k);
                Solution s2 = vnd.algorithm(s1, lMax);
                double newValue = s2.getValue();
                if (newValue < currentValue) {
                    currentValue = newValue;
                    // Restart the loop from the first neighborhood
                    k = 0;
                    s = s2;
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
        //logger.info("GeneralVNS Value = " + String.format("%.3f", bestValue) + " Gap = " + String.format("%.3f", gap) + " Execution Time = " + executionTime + " Acceptable = " + s.isAcceptable());

        s.setValue(bestValue);

        return s;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public double getGap() {
        return gap;
    }

    public static void main(String[] args) {
        String instancePath = "src\\main\\resources\\ORLIB-instances\\cap61";
        for (int i = 0; i < 10; i++) {
            Problem problem = new SSCFLP(instancePath, 932615.8);
            GeneralVNS generalVNS = new GeneralVNS(problem);
            Solution solution = generalVNS.algorithm(problem.getSolution(),3, 3, 5);
        }
    }
}
