package com.sscflp.neighborhood;

import com.sscflp.common.Problem;
import com.sscflp.common.Solution;

import java.util.ArrayList;
import java.util.List;

public class SwapNeighborhood extends Neighborhood {
    private Problem problem;
    private Solution solution;
    private List<Boolean> solutionArrayList;

    public SwapNeighborhood(Problem problem) {
        super();
        this.problem = problem;
        this.solution = problem.getSolution();
        this.solutionArrayList = solution.getSolutionArrayList();
        setNeighborhood();
    }

    public void setNeighborhood() {
        clear();

        // Iterate through each pair of bits in the solution
        for (int i = 0; i < solutionArrayList.size() - 1; i++) {
            for (int j = i + 1; j < solutionArrayList.size(); j++) {
                List<Boolean> solutionList = new ArrayList<>(solutionArrayList);

                // Create a neighbor by swapping two bits with different values
                if (solutionList.get(i) != solutionList.get(j)) {
                    solutionList.set(i, !solutionList.get(i));
                    solutionList.set(j, !solutionList.get(j));
                    Solution s = new Solution(solutionArrayList.size());
                    s.setSolutionArrayList(solutionList);
                    s = problem.calculateObjective(s);
                    if (s.isAcceptable()) {
                        add(s);
                    }
                }
            }
        }
        setBestNeighbor();
    }
}
