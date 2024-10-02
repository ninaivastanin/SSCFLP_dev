package com.sscflp.neighborhood;

import com.sscflp.common.Problem;
import com.sscflp.common.Solution;

import java.util.ArrayList;
import java.util.List;

public class OpenFacilityNeighborhood extends Neighborhood {
    private Problem problem;
    private Solution solution;
    private List<Boolean> solutionArrayList;

    public OpenFacilityNeighborhood(Problem problem) {
        super();
        this.problem = problem;
        this.solution = problem.getSolution();
        this.solutionArrayList = solution.getSolutionArrayList();
        setNeighborhood();
    }

    public void setNeighborhood() {
        clear();

        // Iterate through each bit in the solution
        for (int i = 0; i < solutionArrayList.size(); i++) {
            List<Boolean> solutionList = new ArrayList<>(solutionArrayList);

            // Create a neighbor by inverting one bit in solutionArrayList from false to true
            if (!solutionList.get(i)) {
                solutionList.set(i, !solutionList.get(i));

                Solution s = new Solution(solutionArrayList.size());
                s.setSolutionArrayList((ArrayList<Boolean>) solutionList);
                s = problem.calculateObjective(s);
                if (s.isAcceptable()) {
                    add(s);
                }
            }
        }
        setBestNeighbor();
    }
}
