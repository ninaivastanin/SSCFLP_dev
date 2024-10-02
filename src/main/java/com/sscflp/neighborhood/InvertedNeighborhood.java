package com.sscflp.neighborhood;

import com.sscflp.common.Problem;
import com.sscflp.common.Solution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class InvertedNeighborhood extends Neighborhood {
    private Problem problem;
    private Solution solution;
    private List<Boolean> solutionArrayList;

    private static final Logger logger = LogManager.getLogger(InvertedNeighborhood.class);
    public InvertedNeighborhood(Problem problem, int k) {
        super();
        this.problem = problem;
        this.solution = problem.getSolution();
        this.solutionArrayList = solution.getSolutionArrayList();
        setNeighborhood(k);
    }

    public void setNeighborhood(int k) {
        clear();

        List<Integer> inverted = new ArrayList<>();

        if (k == 0) {
            for (int i = 0; i < solutionArrayList.size(); i++) {
                inverted.clear();
                inverted.add(i);
                invert(inverted);
            }
        } else if (k == 1) {
            for (int i = 0; i < solutionArrayList.size() - 1; i++) {
                for (int j = i + 1; j < solutionArrayList.size(); j++) {
                    inverted.clear();
                    inverted.add(i);
                    inverted.add(j);
                    invert(inverted);
                }
            }
        } else if (k == 2) {
            // Iterate through three indexes
            for (int i = 0; i < solutionArrayList.size() - 2; i++) {
                for (int j = i + 1; j < solutionArrayList.size() - 1; j++) {
                    for (int l = i + 2; l < solutionArrayList.size(); l++) {
                        inverted.clear();
                        inverted.add(i);
                        inverted.add(j);
                        inverted.add(l);
                        invert(inverted);
                    }
                }
            }
        } else {
            logger.info("Invalid value of k.");
            return;
        }

        setBestNeighbor();
    }

    private void invert(List<Integer> inverted) {
        List<Boolean> solutionList = new ArrayList<>(solutionArrayList);
        for (Integer i : inverted) {
            solutionList.set(i, !solutionList.get(i));
        }

        Solution s = new Solution(solutionArrayList.size(), solutionList);
        s = problem.calculateObjective(s);
        if (s.isAcceptable()) {
            add(s);
        }
    }
}
