package com.sscflp.data;

import com.sscflp.entity.Customer;
import com.sscflp.entity.Facility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class InputData {
    private static int nbResources;
    private static int nbCustomers;
    private static List<Facility> facilities;
    private static List<Customer> customers;
    private double optimalSolution;

    private static final Logger logger = LogManager.getLogger(InputData.class);

    public InputData(String instancePath, double optimalSolution) {
        super();

        try {
            // Getting a current instance information
            readInstance(instancePath);
        } catch (IOException ioe) {
            logger.error("Problem with reading file: " + ioe.getMessage());
        } catch (NumberFormatException nfe) {
            logger.error("The wrong format of data in the file: " + nfe.getMessage());
        }
        this.optimalSolution = optimalSolution;
        //logger.info("Optimal solution for instance: " + optimalSolution);
    }

    public void readInstance(String instancePath) throws IOException, NumberFormatException {
        //logger.info("Loading information for the instance " + (new File(instancePath).getName()));

        int i, j;
        double capacity;
        double fixedCost;

        BufferedReader input = new BufferedReader(new FileReader(instancePath));

        // Reading the number of resources and customers
        String line = input.readLine();
        if (line != null) {
            StringTokenizer tokenizer = new StringTokenizer(line.trim(), " ");
            nbResources = Integer.parseInt(tokenizer.nextToken());
            nbCustomers = Integer.parseInt(tokenizer.nextToken());
            //logger.info("nbResources = " + nbResources + " nbCustomers = " + nbCustomers);
        }

        // Reading resources information
        facilities = new ArrayList<>(nbResources);
        for (j = 0; j < nbResources; j++) {
            line = input.readLine();

            if (line != null) {
                // Remove all leading and trailing spaces in the line
                line = line.trim();

                StringTokenizer tokenizer = new StringTokenizer(line, " ");
                capacity = Double.parseDouble(tokenizer.nextToken());
                fixedCost = Double.parseDouble(tokenizer.nextToken());

                facilities.add(new Facility(j, fixedCost, capacity));
            }
        }
        //facilities.forEach(System.out::println);
        //logger.info(facilities.toString());

        // Reading customers information
        customers = new ArrayList<>(nbCustomers);

        // Reading the customers demands
        line = input.readLine();
        if (line != null) {
            // Remove all leading and trailing spaces in the line
            line = line.trim();

            StringTokenizer tokenizer = new StringTokenizer(line, " ");
            for (i = 0; i < nbCustomers; i++) {
                customers.add(new Customer(i, Double.parseDouble(tokenizer.nextToken()), nbResources));
            }
        }

        // Reading the cost of allocating all the demand of customer i to facility j
        for (j = 0; j < nbResources; j++) {
            line = input.readLine();

            if (line != null) {
                // Remove all leading and trailing spaces in the line
                line = line.trim();

                StringTokenizer tokenizer = new StringTokenizer(line, " ");
                for (i = 0; i < nbCustomers; i++) {
                    customers.get(i).addCustomerCost(tokenizer.nextToken());
                }
            }
        }

        //customers.forEach(System.out::println);
        //logger.info(customers.toString());

        input.close();
    }

    public int getNbResources() {
        return nbResources;
    }

    public int getNbCustomers() {
        return nbCustomers;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public double getOptimalSolution() {
        return optimalSolution;
    }
}
