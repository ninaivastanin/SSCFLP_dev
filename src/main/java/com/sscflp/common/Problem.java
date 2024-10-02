package com.sscflp.common;

import com.sscflp.entity.Facility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public interface Problem {
    static final Logger logger = LogManager.getLogger(Problem.class);
    Solution calculateObjective(Solution s);
    void cleanServedCustomersFromAllFacilities();
    boolean isSolutionAcceptable(Solution s);
    double getOptimalValue();
    Solution getSolution();
    void setSolution(Solution s);
    int getNbResources();
    List<Facility> getFacilities();
    default public List<Facility> getOpenedFacilities(Solution s) {
        List<Facility> openedFacilities = new ArrayList<>();
        for (int j = 0; j < getNbResources(); j++) {
            // If resource is established (solution array list element is true), add it to the openedFacilities ArrayList
            if (s.getSolutionArrayListElement(j))
                openedFacilities.add(getFacilities().get(j));
        }

        // Sort opened facilities ArrayList in descending order by capacity
        //openedFacilities.sort(Collections.reverseOrder());
        return openedFacilities;
    }
}
