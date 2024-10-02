package com.sscflp.entity;

import com.sscflp.common.SSCFLP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Customer implements Comparable<Customer> {
    private int id;
    private double demand;
    private List<Double> customerCosts;
    private List<SSCFLP.FacilityCost> customerCostsForOpenedFacilities;

    public Customer(int id, double demand, int nbResources) {
        this.id = id;
        this.demand = demand;
        this.customerCosts = new ArrayList<>(nbResources);
        this.customerCostsForOpenedFacilities = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
    }

    public List<Double> getCustomerCosts() {
        return customerCosts;
    }
    public List<SSCFLP.FacilityCost> getCustomerCostsForOpenedFacilities() {
        Collections.sort(customerCostsForOpenedFacilities);
        return customerCostsForOpenedFacilities;
    }
    public void clearCustomerCostsForOpenedFacilities() {
        customerCostsForOpenedFacilities.clear();
    }

    public void setCustomerCosts(ArrayList<Double> customerCosts) {
        this.customerCosts = customerCosts;
    }

    public void addCustomerCost(String cost) {
        customerCosts.add(Double.parseDouble(cost));
    }
    public void addCustomerCostForOpenedFacility(SSCFLP.FacilityCost cost) {
        customerCostsForOpenedFacilities.add(cost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getId() == customer.getId() && Double.compare(getDemand(), customer.getDemand()) == 0 && Objects.equals(getCustomerCosts(), customer.getCustomerCosts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDemand(), getCustomerCosts());
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", demand=" + demand +
                ", customerCosts=" + customerCosts +
                '}';
    }

    public int compareTo(Customer customer) {
        return Double.compare(getDemand(), customer.getDemand());
    }
}
