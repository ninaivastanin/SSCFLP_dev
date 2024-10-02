
import com.sscflp.algorithms.GeneralVNS;
import com.sscflp.algorithms.ReducedVNS;
import com.sscflp.algorithms.VNS;
import com.sscflp.common.Problem;
import com.sscflp.common.Result;
import com.sscflp.common.SSCFLP;
import com.sscflp.common.Solution;
import com.sscflp.data.OptimalSolution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class SSCFLP_Test {
    private static final String INSTANCES_PATH = "src\\main\\resources\\ORLIB-instances\\";
    private HashMap<String, Double> optimalSolutionHashMap;
    private static List<String> instancesFileNames;

    private static final Logger logger = LogManager.getLogger(SSCFLP_Test.class);

    public SSCFLP_Test(String optimalSolutionsFile, int maxIterations, int kMax, int lMax, int numOfExecutions, String algorithmType) {
        super();

        // Getting the optimal solutions HashMap
        OptimalSolution optimalSolution = new OptimalSolution(INSTANCES_PATH + optimalSolutionsFile);
        optimalSolutionHashMap = optimalSolution.getOptimalSolutionHashMap();

        // Getting the instances file names
        instancesFileNames = optimalSolution.getInstancesFileNames();
        //logger.info("Instances file names: " + instancesFileNames.toString());

        for (String fileName : instancesFileNames) {
            String instancePath = INSTANCES_PATH + fileName;
            double optimalValue = optimalSolutionHashMap.get(fileName);
            logger.info("Instance: " + fileName + ", Optimal value: " + optimalValue + ", maxIterations: " + maxIterations + ", kMax: " + kMax + ", lMax: " + lMax + ", numOfExecutions: " + numOfExecutions);
            logger.info("==========================================================================================================");
            VNSTest(instancePath, optimalValue, maxIterations, kMax, lMax, numOfExecutions, algorithmType);
        }
    }

    public void VNSTest(String instancePath, double optimalValue, int maxIterations, int kMax, int lMax, int numOfExecutions, String algorithmType) {
        VNS vns;
        GeneralVNS gvns;
        Solution solution;
        long executionTime, averageExecutionTime = 0;
        double gap;
        List<Result> testResults = new ArrayList<>(numOfExecutions);
        int numOfAcceptableResults = 0;

        for (int i = 0; i < numOfExecutions; i++) {
            Problem problem = new SSCFLP(instancePath, optimalValue);
            switch (algorithmType) {
                case "RVNS":
                    vns = new ReducedVNS(problem);
                    solution = vns.algorithm(problem.getSolution(),kMax, maxIterations);
                    executionTime = vns.getExecutionTime();
                    gap = vns.getGap();
                    break;
                case "VNS":
                    vns = new VNS(problem);
                    solution = vns.algorithm(problem.getSolution(),kMax, maxIterations);
                    executionTime = vns.getExecutionTime();
                    gap = vns.getGap();
                    break;
                case "GVNS":
                    gvns = new GeneralVNS(problem);
                    solution = gvns.algorithm(problem.getSolution(), kMax, lMax, maxIterations);
                    executionTime = gvns.getExecutionTime();
                    gap = gvns.getGap();
                    break;
                default:
                    gvns = new GeneralVNS(problem);
                    solution = gvns.algorithm(problem.getSolution(), kMax, lMax, maxIterations);
                    executionTime = gvns.getExecutionTime();
                    gap = gvns.getGap();
                    break;
            }

            problem.setSolution(solution);

            Result result = new Result(problem, gap, executionTime);
            logger.info(algorithmType + ": " + result.toString());

            if (solution.isAcceptable()) {
                numOfAcceptableResults ++;
                testResults.add(result);
                averageExecutionTime += executionTime;
            }
        }

        Result bestResult = testResults
                .stream()
                .min(Comparator.comparing(Result::getGap)).get();
        averageExecutionTime = averageExecutionTime / numOfAcceptableResults;
        bestResult.setExecutionTime(averageExecutionTime);

        logger.info("==========================================================================================================");
        logger.info(algorithmType + ": Best " + bestResult.toString());
        bestResult.printResultDetails();
        logger.info("==========================================================================================================");
    }

    public static void main(String[] args) {
        String[] algorithmTypes = {"RVNS", "VNS", "GVNS"};
        SSCFLP_Test test = new SSCFLP_Test("capopt_SSCFLP.txt", 10, 3, 3, 10, algorithmTypes[2]);
    }
}
