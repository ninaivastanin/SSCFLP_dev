package com.sscflp.common;

import com.sscflp.data.InputData;
import com.sscflp.entity.Customer;
import com.sscflp.entity.Facility;

import java.util.*;

public class SSCFLP implements Problem {

    private double optimalValue;
    private int nbResources;
    private int nbCustomers;
    private List<Facility> facilities;
    private List<Customer> customers;
    private double sumCustomerDemands;
    private Solution solution;

    public static class FacilityCost implements Comparable<FacilityCost> {
        int facilityIndex;
        double cost;
        FacilityCost(int facilityIndex, double cost) {
            this.facilityIndex = facilityIndex;
            this.cost = cost;
        }

        public int getFacilityIndex() {
            return facilityIndex;
        }

        public double getCost() {
            return cost;
        }

        public int compareTo(FacilityCost facilityCost) {
            return Double.compare(getCost(), facilityCost.getCost());
        }

        @Override
        public String toString() {
            return "FacilityCost{" +
                    "facilityIndex=" + facilityIndex +
                    ", cost=" + cost +
                    '}';
        }
    }

    public SSCFLP(String instancePath, double optimalValue) {
        InputData inputData = new InputData(instancePath, optimalValue);
        this.optimalValue = optimalValue;
        this.nbResources = inputData.getNbResources();
        this.nbCustomers = inputData.getNbCustomers();
        this.facilities = inputData.getFacilities();
        this.customers = inputData.getCustomers();
        this.sumCustomerDemands = getSumOfAllCustomersDemands();
        this.solution = new Solution(nbResources);
        correctSolutionIfNeeded(solution);
    }

    // If the sum of the user's demands is greater than the sum of open resources capacities,
    // solution array list must be corrected by opening one or more resources
    public void correctSolutionIfNeeded(Solution s) {
        while (!s.hasEstablishedResources() || (sumCustomerDemands > getSumOfAllOpenedFacilitiesCapacities(s))) {
            s.openResource();
        }
    }

    public Solution calculateObjective(Solution s) {
        int i, j;
        double value = 0.0;

        // If solution don't have established resources
        // or sum of the user's demands is greater than the sum of open resources capacities
        // solution isn't acceptable
        if (!s.hasEstablishedResources() || sumCustomerDemands > getSumOfAllOpenedFacilitiesCapacities(s)) {
            s.setAcceptable(false);
            return s;
        }

        // Removing all served customers from all facilities
        cleanServedCustomersFromAllFacilities();

        // Used facilities ArrayList (initially all facilities are unused)
        List<Boolean> usedFacilities = new ArrayList<Boolean>(Arrays.asList(new Boolean[nbResources]));
        Collections.fill(usedFacilities, Boolean.FALSE);

        // For all customers create ArrayList of costs for opened facilities
        clearAllCustomerCostsForOpenedFacilities();
        for (j = 0; j < nbResources; j++) {
            if (s.getSolutionArrayListElement(j)) {
                for (i = 0; i < nbCustomers; i++) {
                    customers.get(i).addCustomerCostForOpenedFacility(new FacilityCost(j, customers.get(i).getCustomerCosts().get(j)));
                }
            }
        }

        List<Integer> unservedCustomersIndexes = new ArrayList<>();

        // Assigning customers to open facilities - customers are sorted in descending order by demand
        List<Customer> sortedByDemandCustomers = new ArrayList<>(customers);
        Collections.sort(sortedByDemandCustomers, Collections.reverseOrder());

        Map<Integer,ArrayList<Integer>> solutionDetailsMap = new HashMap<Integer, ArrayList<Integer>>();

        for (Customer customer : sortedByDemandCustomers) {
            boolean isAssigned = false;
            //logger.info("customer " + i + " getCustomerCostsForOpenedFacilities = " + customer.getCustomerCostsForOpenedFacilities().toString());
            // For all opened facilities costs, started from the smallest cost
            for (FacilityCost cost : customer.getCustomerCostsForOpenedFacilities()) {
                int facilityIndex = cost.getFacilityIndex();
                if (facilities.get(facilityIndex).canAddCustomer(customer)) {
                    //  The  allocation cost is added to the solution value
                    value += cost.getCost();

                    // Facility is marked as used
                    usedFacilities.set(facilityIndex, true);

                    // Add served customer to resource
                    facilities.get(facilityIndex).addCustomer(customer);
                    solutionDetailsMap.computeIfAbsent(facilityIndex, k -> new ArrayList<>()).add(customer.getId());
                    isAssigned = true;
                    break;
                }
            }

            if (!isAssigned) {
                unservedCustomersIndexes.add(customer.getId());
            }
        }

        if (!unservedCustomersIndexes.isEmpty()) {
            logger.info("Unserved customers:");
            for (int index : unservedCustomersIndexes)
                logger.info("index = " + index + " demand = " + customers.get(index).getDemand());
        }

        // The cost of establishing all used facilities is added to the solution value
        for (j = 0; j < nbResources; j++) {
            if (!usedFacilities.get(j)) continue;
            value += facilities.get(j).getFixedCost();
        }

        // The solution array list is changed so that its values contain currently used resources
        for (j = 0; j < nbResources; j++) {
            s.setSolutionArrayListElement(j, usedFacilities.get(j));
        }

        s.setValue(value);
        s.setSolutionDetailsMap(solutionDetailsMap);
        s.setAcceptable(isSolutionAcceptable(s));

        return s;
    }
    public double getSumOfAllCustomersDemands() {
        return customers.stream()
                .map(Customer::getDemand)
                .reduce(0.0, Double::sum);
    }

    public double getSumOfAllOpenedFacilitiesCapacities(Solution s) {
        List<Facility> openedFacilities = getOpenedFacilities(s);
        return openedFacilities.stream()
                .map(Facility::getCapacity)
                .reduce(0.0, Double::sum);
    }

    // Function for clearing all served customers from all facilities
    public void cleanServedCustomersFromAllFacilities() {
        for (int i = 0; i < nbResources; i++) {
            facilities.get(i).cleanServedCustomers();
        }
    }

    public void clearAllCustomerCostsForOpenedFacilities() {
        for (int i = 0; i < nbCustomers; i++) {
            customers.get(i).clearCustomerCostsForOpenedFacilities();
        }
    }

    public boolean isSolutionAcceptable(Solution s) {
        int numOfServedCustomers = 0;
        boolean capacityRespected = true;

        for (int j = 0; j < nbResources; j++) {
            // Skip unestablished resources
            if (!s.getSolutionArrayListElement(j)) continue;

            numOfServedCustomers += facilities.get(j).getServedCustomers().size();

            // The sum of all served customer demands must not be
            //greater than the capacity of assigned resource
            if (facilities.get(j).getUsedCapacity() > facilities.get(j).getCapacity()) {
                capacityRespected = false;
                break;
            }
        }
        //logger.info("Number of the served customers = " + numOfServedCustomers + " capacityRespected = " + capacityRespected);
        return s.hasEstablishedResources() && (numOfServedCustomers == nbCustomers) && capacityRespected;
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

    @Override
    public List<Facility> getFacilities() {
        return facilities;
    }
}
