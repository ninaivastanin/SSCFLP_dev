package com.sscflp.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public class Result {
    Problem problem;
    Solution solution;
    double gap;
    long executionTime;

    private static final Logger logger = LogManager.getLogger(Result.class);

    public Result(Problem problem, double gap, long executionTime) {
        this.problem = problem;
        this.solution = problem.getSolution();
        this.gap = gap;
        this.executionTime = executionTime;
    }

    public Problem getProblem() {
        return problem;
    }

    public double getGap() {
        return gap;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "Value= " + String.format("%.3f", solution.getValue()) + ", Gap = " + String.format("%.3f", gap) + ", Average Execution Time = " + executionTime + ", Acceptable = " + solution.isAcceptable();
    }

    public void printResultDetails() {
        List<Integer> openedFacilitiesindexesList = new ArrayList<>(solution.getSolutionDetailsMap().keySet());
        Collections.sort(openedFacilitiesindexesList);
        logger.info("Set of open facilities: " + openedFacilitiesindexesList.toString());

        for (Integer facilityIndex : openedFacilitiesindexesList) {
            ArrayList<Integer> customersIndexes = solution.getSolutionDetailsMap().get(facilityIndex);
            Collections.sort(customersIndexes);
            logger.info(facilityIndex + " : " + customersIndexes.toString());
        }
    }
}

