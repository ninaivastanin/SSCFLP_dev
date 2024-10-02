package com.sscflp.entity;

import java.util.*;
import java.util.stream.Collectors;

public class Facility implements Comparable<Facility> {
    private int id;
    private double fixedCost;
    private double capacity;
    private HashSet<Customer> servedCustomers;
    private List servedCustomersIdsList;
    private double usedCapacity;

    public Facility(int id, double fixedCost, double capacity) {
        this.id = id;
        this.fixedCost = fixedCost;
        this.capacity = capacity;
        this.servedCustomers = new HashSet<>();
        this.servedCustomersIdsList  = new ArrayList<Integer>();
        this.usedCapacity = 0.0D;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(double fixedCost) {
        this.fixedCost = fixedCost;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public HashSet<Customer> getServedCustomers() {
        return servedCustomers;
    }

    public boolean canAddCustomer(Customer customer) {
        return ((usedCapacity + customer.getDemand()) <= capacity) ? true : false;
    }

    public void addCustomer(Customer customer) {
        if (servedCustomers.add(customer)) usedCapacity += customer.getDemand();
    }

    public void removeCustomer(Customer customer) {
        if (servedCustomers.remove(customer)) usedCapacity -= customer.getDemand();
    }

    public void cleanServedCustomers() {
        servedCustomers.clear();
        usedCapacity = 0.0D;
    }

    public List getServedCustomersIdsList() {
        List<Integer> servedCustomersIdsList = servedCustomers.stream()
                                                .map(Customer::getId)
                                                .collect(Collectors.toList());
        Collections.sort(servedCustomersIdsList);
        return servedCustomersIdsList;
    }


    public double getUsedCapacity() {
        return usedCapacity;
    }

    @Override
    public int compareTo(Facility facility) {
        return Double.compare(getCapacity(), facility.getCapacity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFixedCost(), getCapacity());
    }

    @Override
    public String toString() {
        return "Facility{" +
                "id=" + id +
                ", fixedCost=" + fixedCost +
                ", capacity=" + capacity +
                ", servedCustomers=" + servedCustomers +
                ", usedCapacity=" + usedCapacity +
                '}';
    }
}
