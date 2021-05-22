package MLCQ.Researching;

import CodeSmells.CodeSmell;
import DesigniteJava.DesigniteResultParser;
import DesigniteJava.Smells.SmellShortInfo;
import MLCQ.AnalyzerMLCQ;
import MLCQ.CodeReview;
import MLCQ.Smells.MLCQCodeSmell;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ResearchingMLCQProgram {

    public static HashMap<String, SmellShortInfo> parseImplementationSmells(String dir, String packageName, int[] wantedSmells) throws IOException {
        DesigniteResultParser parser = new DesigniteResultParser(dir, ',');

        Reader reader = new FileReader(dir + "/" + packageName + "/ImplementationSmells.csv");
        BufferedReader buffReader = new BufferedReader(reader);

        HashMap<String, SmellShortInfo> implementationSmells = parser.parseImplementationSmells(buffReader, wantedSmells);

        reader.close();
        buffReader.close();

        return implementationSmells;
    }

    public static void main(String[] args) {
        String pathMLCQ = "./results/mlcq/csv/final-code-reviews.csv";
        String pathDesigniteJava = "./results/designite-java/raw-output";

        try {
            AnalyzerMLCQ analyzer = new AnalyzerMLCQ(pathMLCQ, "./logs", ';');

            Reader mlcqReader = new FileReader(pathMLCQ);
            Reader mlcqBuffRbuffReader = new BufferedReader(mlcqReader);

            List<CodeReview> reviewsMLCQ = analyzer.prepareReviews(mlcqBuffRbuffReader);

            mlcqReader.close();
            mlcqBuffRbuffReader.close();

            int[] wantedImplementationSmells = { CodeSmell.LONG_METHOD };
            DesigniteResultParser parser = new DesigniteResultParser(pathDesigniteJava, ',');

            //
            // COM
            //
            HashMap<String, SmellShortInfo> implementationSmellsCom = parseImplementationSmells(pathDesigniteJava, "com", wantedImplementationSmells);

            System.out.println("Com loaded.");

            //
            // FROM A TO I
            //
            HashMap<String, SmellShortInfo> implementationSmellsAI = parseImplementationSmells(pathDesigniteJava, "__from-a-to-i", wantedImplementationSmells);

            System.out.println("From A to I loaded.");

            //
            // FROM J TO Z
            //
            HashMap<String, SmellShortInfo> implementationSmellsJZ = parseImplementationSmells(pathDesigniteJava, "__from-j-to-z", wantedImplementationSmells);

            System.out.println("From J to Z loaded.");

            //
            // ORG
            //
            HashMap<String, SmellShortInfo> implementationSmellsOrg = parseImplementationSmells(pathDesigniteJava, "org", wantedImplementationSmells);

            System.out.println("Org loaded.");


            HashMap<String, SmellShortInfo> implementationSmells = new HashMap<>();
            implementationSmells.putAll(implementationSmellsCom);
            implementationSmells.putAll(implementationSmellsAI);
            implementationSmells.putAll(implementationSmellsJZ);
            implementationSmells.putAll(implementationSmellsOrg);

            LinkedList<String> noneFits = new LinkedList<>();

            LinkedList<String> longMethodFits = new LinkedList<>();
            LinkedList<String> longMethodFalsePositives = new LinkedList<>();
            LinkedList<String> longMethodFalseNegative = new LinkedList<>();

            SmellShortInfo smellInfo;
            String componentID;

            for (CodeReview review : reviewsMLCQ) {
                componentID = review.getPreparedCodeName();

                if (review.codeSmell.equals(MLCQCodeSmell.LONG_METHOD)) {
                    if (review.severity.equals(MLCQCodeSmell.NONE)) {
                        if (!implementationSmells.containsKey(componentID)) { // true negative
                            noneFits.add(componentID);
                        } else {
                            longMethodFalsePositives.add(componentID); // false positive
                        }
                    } else {
                        if (implementationSmells.containsKey(componentID)) { // true positive
                            smellInfo = implementationSmells.get(componentID);

                            if (smellInfo.hasSmell(CodeSmell.LONG_METHOD)) {
                                longMethodFits.add(componentID);
                            }
                        } else { // false negative
                            longMethodFalseNegative.add(componentID);
                        }
                    }
                }
            }

            System.out.println("Long method fits (true positive): " + longMethodFits.size());

//            for (String lmFits : longMethodFits) {
//                System.out.println(lmFits);
//            }

            System.out.println("\n\nLong method false positives: " + longMethodFalsePositives.size());

//            for (String lmFalsePositives : longMethodFalsePositives) {
//                System.out.println(lmFalsePositives);
//            }

            System.out.println("\n\nNone fits (true negative): " + noneFits.size());

            System.out.println("\n\nLong method false negative: " + longMethodFalseNegative.size());
//
//            for (String fits : noneFits) {
//                System.out.println(fits);
//            }
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
            System.out.println(e.toString());
        }
    }

}
