package MLCQ.Researching;

import CodeSmells.CodeSmell;
import DesigniteJava.DesigniteResultParser;
import DesigniteJava.Smells.DesignSmell;
import DesigniteJava.Smells.ImplementationSmell;
import DesigniteJava.Smells.SmellShortInfo;
import MLCQ.AnalyzerMLCQ;
import MLCQ.CodeReview;
import MLCQ.Smells.MLCQCodeSmell;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ResearchingMLCQProgram {

    public static HashMap<String, SmellShortInfo> parseSmells(String dir, String packageName, Class clazz, int[] wantedSmells) throws IOException {
        String filename;

        if (clazz == ImplementationSmell.class) {
            filename = "ImplementationSmells.csv";
        } else if (clazz == DesignSmell.class) {
            filename = "DesignSmells.csv";
        } else {
            filename = "";
        }

        DesigniteResultParser parser = new DesigniteResultParser(dir, ',');

        Reader reader = new FileReader(dir + "/" + packageName + "/" + filename);
        BufferedReader buffReader = new BufferedReader(reader);

        HashMap<String, SmellShortInfo> implementationSmells = parser.parseImplementationSmells(buffReader, clazz, wantedSmells);

        reader.close();
        buffReader.close();

        return implementationSmells;
    }

    public static void main(String[] args) {
        String pathMLCQ = "./results/mlcq/csv/final-code-reviews.csv";
        String pathDesigniteJava = "./results/designite-java/parsed-output";
        Class clazz = DesignSmell.class;
        String smellName = "FEATURE ENVY";
        String mlcqSmellName = MLCQCodeSmell.FEATURE_ENVY;
        int smellCode = CodeSmell.FEATURE_ENVY;

        try {
            AnalyzerMLCQ analyzer = new AnalyzerMLCQ(pathMLCQ, "./logs", ';');

            Reader mlcqReader = new FileReader(pathMLCQ);
            Reader mlcqBuffRbuffReader = new BufferedReader(mlcqReader);

            List<CodeReview> reviewsMLCQ = analyzer.prepareReviews(mlcqBuffRbuffReader);

            mlcqReader.close();
            mlcqBuffRbuffReader.close();

            int[] wantedSmells = { smellCode };
            DesigniteResultParser parser = new DesigniteResultParser(pathDesigniteJava, ',');

            //
            // COM
            //
            HashMap<String, SmellShortInfo> smellsCom = parseSmells(pathDesigniteJava, "com", clazz, wantedSmells);

            System.out.println("Com loaded.");

            //
            // FROM A TO I
            //
            HashMap<String, SmellShortInfo> smellsAI = parseSmells(pathDesigniteJava, "__from-a-to-i", clazz, wantedSmells);

            System.out.println("From A to I loaded.");

            //
            // FROM J TO Z
            //
            HashMap<String, SmellShortInfo> smellsJZ = parseSmells(pathDesigniteJava, "__from-j-to-z", clazz, wantedSmells);

            System.out.println("From J to Z loaded.");

            //
            // ORG
            //
            HashMap<String, SmellShortInfo> smellsOrg = parseSmells(pathDesigniteJava, "org", clazz, wantedSmells);

            System.out.println("Org loaded.");


            HashMap<String, SmellShortInfo> smells = new HashMap<>();
            smells.putAll(smellsCom);
            smells.putAll(smellsAI);
            smells.putAll(smellsJZ);
            smells.putAll(smellsOrg);

            LinkedList<String> noneFits = new LinkedList<>();
            LinkedList<String> smellFits = new LinkedList<>();
            LinkedList<String> smellFalsePositives = new LinkedList<>();
            LinkedList<String> smellFalseNegative = new LinkedList<>();

            SmellShortInfo smellInfo;
            String componentID;

            for (CodeReview review : reviewsMLCQ) {
                componentID = review.getPreparedCodeName();

                if (review.codeSmell.equals(mlcqSmellName)) {
                    if (review.severity.equals(MLCQCodeSmell.NONE)) {
                        if (!smells.containsKey(componentID)) { // true negative
                            noneFits.add(componentID);
                        } else {
                            smellFalsePositives.add(componentID); // false positive
                        }
                    } else {
                        if (smells.containsKey(componentID)) { // true positive
                            smellInfo = smells.get(componentID);

                            if (smellInfo.hasSmell(smellCode)) {
                                smellFits.add(componentID);
                            }
                        } else { // false negative
                            smellFalseNegative.add(componentID);
                        }
                    }
                }
            }

            System.out.println("==== " + smellName + " ====\n");

            System.out.println("Smell fits (true positive): " + smellFits.size());

            for (String fits : smellFits) {
                System.out.println(fits);
            }

            System.out.println("\n\nSmell false positives: " + smellFalsePositives.size());

//            for (String lmFalsePositives : longMethodFalsePositives) {
//                System.out.println(lmFalsePositives);
//            }

            System.out.println("\n\nNone fits (true negative): " + noneFits.size());

            System.out.println("\n\nSmell false negative: " + smellFalseNegative.size());
//
            for (String fits : smellFalseNegative) {
                System.out.println(fits);
            }
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
            System.out.println(e.toString());
        }
    }

}
