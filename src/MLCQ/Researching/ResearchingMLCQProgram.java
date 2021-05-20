package MLCQ.Researching;

import CodeSmells.CodeSmell;
import DesigniteJava.DesigniteResultParser;
import DesigniteJava.Smells.SmellShortInfo;
import MLCQ.AnalyzerMLCQ;
import MLCQ.CodeReview;
import MLCQ.Smells.MLCQCodeSmell;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ResearchingMLCQProgram {

    public static void main(String[] args) {
        String pathMLCQ = "./results/mlcq/csv/final-code-reviews.csv";
        String pathDesigniteJava = "./results/designite-java/raw-output";

        try {
            AnalyzerMLCQ analyzer = new AnalyzerMLCQ(pathMLCQ, "./logs", ';');

            List<CodeReview> reviewsMLCQ = analyzer.prepareReviews(pathMLCQ);

            DesigniteResultParser parser = new DesigniteResultParser(pathDesigniteJava, ',');

            int[] wantedImplementationSmells = { CodeSmell.LONG_METHOD };
            int[] wantedDesignSmells = { CodeSmell.FEATURE_ENVY };

            HashMap<String, SmellShortInfo> implementationSmells = parser.parseImplementationSmells("ImplementationSmells.csv", wantedImplementationSmells);

            LinkedList<String> noneFits = new LinkedList<>();

            LinkedList<String> longMethodFits = new LinkedList<>();
            LinkedList<String> longMethodFalsePositives = new LinkedList<>();

            LinkedList<String> featureEnvyFits = new LinkedList<>();
            LinkedList<String> featureEnvyFalsePositives = new LinkedList<>();

            SmellShortInfo smellInfo;
            String componentID;

            for (CodeReview review : reviewsMLCQ) {
                componentID = review.getPreparedCodeName();

                if (review.codeSmell.equals(MLCQCodeSmell.LONG_METHOD)) {
                    if (review.severity.equals(MLCQCodeSmell.NONE)) {
                        if (!implementationSmells.containsKey(componentID)) {
                            noneFits.add(componentID);
                        } else {
                            longMethodFalsePositives.add(componentID);
                        }
                    } else {
                        if (implementationSmells.containsKey(componentID)) {
                            smellInfo = implementationSmells.get(componentID);

                            if (smellInfo.hasSmell(CodeSmell.LONG_METHOD)) {
                                longMethodFits.add(componentID);
                            }
                        }
                    }
                } else if (review.codeSmell.equals(MLCQCodeSmell.FEATURE_ENVY)) {
                    if (review.severity.equals(MLCQCodeSmell.NONE)) {
                        if (!implementationSmells.containsKey(componentID)) {
                            noneFits.add(componentID);
                        } else {
                            featureEnvyFalsePositives.add(componentID);
                        }
                    } else {
                        if (implementationSmells.containsKey(componentID)) {
                            smellInfo = implementationSmells.get(componentID);

                            if (smellInfo.hasSmell(CodeSmell.FEATURE_ENVY)) {
                                featureEnvyFits.add(componentID);
                            }
                        }
                    }
                }
            }

            System.out.println("Long method fits: " + longMethodFits.size());

            for (String lmFits : longMethodFits) {
                System.out.println(lmFits);
            }

            System.out.println("\n\nLong method false positives: " + longMethodFalsePositives.size());

            for (String lmFalsePositives : longMethodFalsePositives) {
                System.out.println(lmFalsePositives);
            }

            System.out.println("\n\nFeature envy fits: " + featureEnvyFits.size());

            for (String feFits : featureEnvyFits) {
                System.out.println(feFits);
            }

            System.out.println("\n\nFeature envy false positives: " + featureEnvyFalsePositives.size());

            for (String feFalsePositives : featureEnvyFalsePositives) {
                System.out.println(feFalsePositives);
            }

            System.out.println("\n\nNone fits: " + noneFits.size());

            for (String fits : noneFits) {
                System.out.println(fits);
            }
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
            System.out.println(e.toString());
        }
    }

}
