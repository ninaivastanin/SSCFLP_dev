package com.sscflp.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class OptimalSolution {
    // The hash map that stores information about optimal solution value for each instance
    // This information is loaded from the capopt_SSCFLP.txt file
    private HashMap<String, Double> optimalSolutionHashMap;

    private static final Logger logger = LogManager.getLogger(OptimalSolution.class);

    public OptimalSolution(String optimalSolutionsPath) {
        try {
            // Loading optimal solution values for all instances from the capopt_SSCFLP.txt file
            loadOptimalSolutionValues(optimalSolutionsPath);
        } catch (IOException ioe) {
            logger.error("Problem with reading the file: " + ioe.getMessage());
        } catch (NumberFormatException nfe) {
            logger.error("The wrong format of data in the file: " + nfe.getMessage());
        }
    }

    public void loadOptimalSolutionValues(String filePath) throws IOException, NumberFormatException {
        optimalSolutionHashMap = new HashMap<>();

        BufferedReader input = new BufferedReader(new FileReader(filePath));
        String delimiter = "/  +/g";

        // Skipping the first line
        String line = input.readLine();

        String capFileName;
        Double optimalSolutionValue;
        while ((line = input.readLine()) != null && !line.trim().isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(line.trim(), delimiter);
            capFileName = tokenizer.nextToken();
            optimalSolutionValue = Double.valueOf(tokenizer.nextToken());
            if (capFileName.startsWith("capa") || capFileName.startsWith("capb") || capFileName.startsWith("capc")) {
                capFileName = capFileName + 1;
                optimalSolutionHashMap.put(capFileName, optimalSolutionValue);
                if ((new File(filePath)).getName().equalsIgnoreCase("capopt_SSCFLP.txt")) {
                    for (int i = 2; i <= 4; i++) {
                        capFileName = capFileName.substring(0, capFileName.length() - 1) + i;
                        line = input.readLine();
                        if (line != null && !line.trim().isEmpty()) {
                            tokenizer = new StringTokenizer(line.trim(), delimiter);
                            optimalSolutionValue = Double.valueOf(tokenizer.nextToken());
                            optimalSolutionHashMap.put(capFileName, optimalSolutionValue);
                        }
                    }
                }
            } else {
                optimalSolutionHashMap.put(capFileName, optimalSolutionValue);
            }
        }
    }

    public List<String> getInstancesFileNames(){
        List<String> instancesFileNames = new ArrayList<>(optimalSolutionHashMap.keySet());
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return extractNumber(o1) - extractNumber(o2);
            }

            private int extractNumber(String name) {
                String number = "";

                int i = 0;
                if (name.startsWith("capa")) {
                    number = name.replaceFirst("capa", "100");
                } else if (name.startsWith("capb")) {
                    number = name.replaceFirst("capb", "200");
                } else if (name.startsWith("capc")) {
                    number = name.replaceFirst("capc", "300");
                } else if (name.startsWith("cap")) {
                    number = name.replaceFirst("cap", "");
                }

                try {
                    i = Integer.parseInt(number);
                } catch (NumberFormatException nfe) {
                    logger.error("Extract number from the instance name: " + nfe.getMessage());
                }
                return i;
            }
        };
        instancesFileNames.sort(comparator);
        return instancesFileNames;
    }

    public HashMap<String, Double> getOptimalSolutionHashMap() {
        return optimalSolutionHashMap;
    }
}
