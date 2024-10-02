package com.sscflp.common;

import com.sscflp.data.InputData;
import com.sscflp.entity.Customer;
import com.sscflp.entity.Facility;

import java.util.*;

public class UFLP implements Problem {
    private static final double POSITIVE_INFINITY = 1e9;

    private double optimalValue;
    private int nbResources;
    private int nbCustomers;
    private List<Facility> facilities;
    private List<Customer> customers;
    private Solution solution;
    public UFLP(String instancePath, double optimalValue) {
        InputData inputData = new InputData(instancePath, optimalValue);
        this.optimalValue = optimalValue;
        this.nbResources = inputData.getNbResources();
        this.nbCustomers = inputData.getNbCustomers();
        this.facilities = inputData.getFacilities();
        this.customers = inputData.getCustomers();
        this.solution = calculateObjective(new Solution(nbResources));
    }

    public Solution calculateObjective(Solution s) {
        int i, j;
        double value = 0.0;

        // If solution don't have established resources
        // solution isn't acceptable
        if (!s.hasEstablishedResources()) {
            s.setAcceptable(false);
            return s;
        }

        // Removing all served customers from all facilities
        cleanServedCustomersFromAllFacilities();

        // All resources are unused at the beginning
        List<Boolean> used = new ArrayList<Boolean>(Arrays.asList(new Boolean[nbResources]));
        Collections.fill(used, Boolean.FALSE);

        Map<Integer,ArrayList<Integer>> solutionDetailsMap = new HashMap<Integer, ArrayList<Integer>>();

        // For each user go through all established resources and select the lowest allocation cost
        for (i = 0; i < nbCustomers; i++) {
            double minCost = POSITIVE_INFINITY;
            int jUsed = -1;
            for (j = 0; j < nbResources; j++) {
                // Skip unestablished resources
                if (!s.getSolutionArrayListElement(j)) continue;

                double currentCost = customers.get(i).getCustomerCosts().get(j);
                if (currentCost < minCost) {
                    minCost = currentCost;
                    jUsed = j;
                }
            }
            //logger.info("User " + i + " minCost = " + minCost + " jUsed = " + jUsed);
            if (jUsed >= 0 && jUsed < nbResources) {
                //  The minimal allocation cost is added to the solution value
                value += minCost;

                // Such resource is marked as used
                used.set(jUsed, true);

                // Add served customer to resource
                facilities.get(jUsed).addCustomer(customers.get(i));
                solutionDetailsMap.computeIfAbsent(jUsed, k -> new ArrayList<>()).add(customers.get(i).getId());
            } else {
                logger.info("Solution: " + s);
                logger.info("User " + i + " minCost = " + minCost  + " jUsed = " + jUsed);
            }
        }

        // The cost of establishing all used resources is added to the solution value
        for (j = 0; j < nbResources; j++) {
            if (!used.get(j)) continue;
            value += facilities.get(j).getFixedCost();
        }

        // The solution array list is changed so that its values contain currently used resources
        for (j = 0; j < nbResources; j++) {
            s.setSolutionArrayListElement(j, used.get(j));
        }

        s.setValue(value);
        s.setSolutionDetailsMap(solutionDetailsMap);
        s.setAcceptable(isSolutionAcceptable(s));

        return s;
    }

    // Function for clearing all served customers from all facilities
    public void cleanServedCustomersFromAllFacilities() {
        for (int i = 0; i < nbResources; i++) {
            facilities.get(i).cleanServedCustomers();
        }
    }

    public boolean isSolutionAcceptable(Solution s) {
        int numOfServedCustomers = 0;

        for (int j = 0; j < nbResources; j++) {
            // Skip unestablished resources
            if (!s.getSolutionArrayListElement(j)) continue;

            numOfServedCustomers += facilities.get(j).getServedCustomers().size();
        }

        //logger.info("Number of the served customers = " + numOfServedCustomers + ", num customers = " + nbCustomers + " solution: " + s.getSolutionArrayList().toString());
        if (numOfServedCustomers != nbCustomers)
            logger.info("Number of the served customers = " + numOfServedCustomers + ", num customers = " + nbCustomers);
        return s.hasEstablishedResources() && (numOfServedCustomers == nbCustomers);
    }

    public double getOptimalValue() {
        return optimalValue;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public int getNbResources() {
        return nbResources;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }
}
