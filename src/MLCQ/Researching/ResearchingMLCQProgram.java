package MLCQ.Researching;

import BinaryClassification.BinaryClassificationResult;
import BinaryClassification.BinaryClassificationStatistics;
import CodeSmells.CodeSmellName;
import DesigniteJava.DesigniteResultParser;
import DesigniteJava.Smells.DesignSmell;
import DesigniteJava.Smells.ImplementationSmell;
import CodeSmells.SmellShortInfo;
import MLCQ.Analysis.PMDFileSmells;
import MLCQ.AnalyzerMLCQ;
import MLCQ.CodeReview;
import MLCQ.Smells.MLCQCodeSmell;
import PMD.PMDResultParser;
import PMD.Smells.PMDCodeSmellName;
import PMD.Smells.PMDSmell;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ResearchingMLCQProgram {

    public static HashMap<String, SmellShortInfo> parseDesigniteSmells(String dir, String packageName, Class clazz, int[] wantedSmells) throws IOException {
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

        HashMap<String, SmellShortInfo> implementationSmells = parser.parseSmells(buffReader, clazz, wantedSmells);

        reader.close();
        buffReader.close();

        return implementationSmells;
    }

    public static HashMap<String, PMDFileSmells> parsePMDSmells(String path, int[] wantedSmells) throws IOException {
        PMDResultParser pmdResultParser = new PMDResultParser();

        Reader reader = new FileReader(path);
        BufferedReader buffReader = new BufferedReader(reader);

        HashMap<String, PMDFileSmells> pmdSmellsCom = pmdResultParser.parseSmells(buffReader, wantedSmells);

        reader.close();
        buffReader.close();

        return pmdSmellsCom;
    }

    public static void main(String[] args) {
        String pathMLCQ = "./results/mlcq/csv/final-code-reviews.csv";
        String pathDesigniteJava = "./results/designite-java/parsed-output";
        Class clazz = DesignSmell.class;
        String smellName = "FEATURE ENVY";
        String mlcqSmellName = MLCQCodeSmell.FEATURE_ENVY;
        int smellCode = CodeSmellName.FEATURE_ENVY;

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
            HashMap<String, SmellShortInfo> smellsCom = parseDesigniteSmells(pathDesigniteJava, "com", clazz, wantedSmells);

            System.out.println("Com loaded.");

            //
            // FROM A TO I
            //
            HashMap<String, SmellShortInfo> smellsAI = parseDesigniteSmells(pathDesigniteJava, "__from-a-to-i", clazz, wantedSmells);

            System.out.println("From A to I loaded.");

            //
            // FROM J TO Z
            //
            HashMap<String, SmellShortInfo> smellsJZ = parseDesigniteSmells(pathDesigniteJava, "__from-j-to-z", clazz, wantedSmells);

            System.out.println("From J to Z loaded.");

            //
            // ORG
            //
            HashMap<String, SmellShortInfo> smellsOrg = parseDesigniteSmells(pathDesigniteJava, "org", clazz, wantedSmells);

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

            // MLCQ Statistics

            int mlcqLongMethodSmells = 0;
            int mlcqBlobSmells = 0;
            int mlcqDataClassSmells = 0;
            int mlcqFeatureEnvySmells = 0;

            int mlcqLongMethodNone = 0;
            int mlcqBlobNone = 0;
            int mlcqDataClassNone = 0;
            int mlcqFeatureEnvyNone = 0;

            // PMD Statistics

            HashMap<String, PMDFileSmells> pmdSmells = new HashMap<>();

            int[] pmdWantedSmells = { CodeSmellName.BLOB, CodeSmellName.DATA_CLASS, CodeSmellName.LONG_METHOD };

            HashMap<String, PMDFileSmells> pmdSmellsCom = parsePMDSmells("./results/pmd/raw-output/com/pmd-report.csv", pmdWantedSmells);
            HashMap<String, PMDFileSmells> pmdSmellsOrg = parsePMDSmells("./results/pmd/raw-output/org/pmd-report.csv", pmdWantedSmells);
            HashMap<String, PMDFileSmells> pmdSmellsFromAToI = parsePMDSmells("./results/pmd/raw-output/__from-a-to-i/pmd-report.csv", pmdWantedSmells);
            HashMap<String, PMDFileSmells> pmdSmellsFromJToZ = parsePMDSmells("./results/pmd/raw-output/__from-j-to-z/pmd-report.csv", pmdWantedSmells);

            pmdSmells.putAll(pmdSmellsCom);
            pmdSmells.putAll(pmdSmellsOrg);
            pmdSmells.putAll(pmdSmellsFromAToI);
            pmdSmells.putAll(pmdSmellsFromJToZ);

            System.out.println(pmdSmells.size());

            BinaryClassificationStatistics pmdBlob = new BinaryClassificationStatistics();
            BinaryClassificationStatistics pmdLongMethod = new BinaryClassificationStatistics();
            BinaryClassificationStatistics pmdDataClass = new BinaryClassificationStatistics();

            PMDFileSmells pmdSmell;
            BinaryClassificationResult binaryClassificationResult;

            BinaryClassificationStatistics djStat = new BinaryClassificationStatistics();

            for (CodeReview review : reviewsMLCQ) {
                componentID = review.getPreparedCodeName();

                if (review.codeSmell.equals(mlcqSmellName)) {
                    if (review.severity.equals(MLCQCodeSmell.NONE)) {
                        if (!smells.containsKey(componentID)) { // true negative
                            djStat.increaseTrueNegatives();
                            noneFits.add(componentID);
                        } else {
                            djStat.increaseFalsePositives();
                            smellFalsePositives.add(componentID); // false positive
                        }
                    } else {
                        if (smells.containsKey(componentID)) { // true positive
                            djStat.increaseTruePositives();
                            smellInfo = smells.get(componentID);

                            if (smellInfo.hasSmell(smellCode)) {
                                smellFits.add(componentID);
                            }
                        } else { // false negative
                            djStat.increaseFalseNegatives();
                            smellFalseNegative.add(componentID);
                        }
                    }
                }

                if (review.codeSmell.equals(MLCQCodeSmell.LONG_METHOD)) {
                    if (review.severity.equals(MLCQCodeSmell.NONE)) {
                        mlcqLongMethodNone++;
                    } else {
                        mlcqLongMethodSmells++;
                    }
                } else if (review.codeSmell.equals(MLCQCodeSmell.BLOB)) {
                    if (review.severity.equals(MLCQCodeSmell.NONE)) {
                        mlcqBlobNone++;
                    } else {
                        mlcqBlobSmells++;
                    }
                } else if (review.codeSmell.equals(MLCQCodeSmell.FEATURE_ENVY)) {
                    if (review.severity.equals(MLCQCodeSmell.NONE)) {
                        mlcqFeatureEnvyNone++;
                    } else {
                        mlcqFeatureEnvySmells++;
                    }
                } else if (review.codeSmell.equals(MLCQCodeSmell.DATA_CLASS)) {
                    if (review.severity.equals(MLCQCodeSmell.NONE)) {
                        mlcqDataClassNone++;
                    } else {
                        mlcqDataClassSmells++;
                    }
                }

                // PMD results
                pmdSmell = pmdSmells.get(review.sampleID + "");

                if (pmdSmell == null) {
                    pmdSmell = new PMDFileSmells(review.sampleID + "");
                }

                if (review.codeSmell.equals(MLCQCodeSmell.BLOB)) {
                    pmdBlob.increase(pmdSmell.checkMLCQReview(review));
                } else if (review.codeSmell.equals(MLCQCodeSmell.DATA_CLASS)) {
                    pmdDataClass.increase(pmdSmell.checkMLCQReview(review));
                } else if (review.codeSmell.equals(MLCQCodeSmell.LONG_METHOD)) {
                    pmdLongMethod.increase(pmdSmell.checkMLCQReview(review));
                }
            }

            System.out.println("==== " + smellName + " ====\n");

            System.out.println("Smell fits (true positive): " + smellFits.size() + " " + djStat.getTruePositives());

//            for (String fits : smellFits) {
//                System.out.println(fits);
//            }

            System.out.println("\n\nSmell false positives: " + smellFalsePositives.size() + " " + djStat.getFalsePositives());

//            for (String lmFalsePositives : longMethodFalsePositives) {
//                System.out.println(lmFalsePositives);
//            }

            System.out.println("\n\nNone fits (true negative): " + noneFits.size() + " " + djStat.getTrueNegatives());

            System.out.println("\n\nSmell false negative: " + smellFalseNegative.size() + " " + djStat.getFalseNegatives());

            System.out.println("MCC: " + djStat.getMCC());

            //
            for (String fits : smellFalseNegative) {
                System.out.println(fits);
            }

            System.out.println("\n\n\nMLCQ smells:");
            System.out.println("Long Method: Existing:" + mlcqLongMethodSmells + ", None:" + mlcqLongMethodNone);
            System.out.println("Data Class: Existing:" + mlcqDataClassSmells + ", None:" + mlcqDataClassNone);
            System.out.println("Blob: Existing:" + mlcqBlobSmells + ", None:" + mlcqBlobNone);
            System.out.println("Feature Envy: Existing:" + mlcqFeatureEnvySmells + ", None:" + mlcqFeatureEnvyNone);

            System.out.println("\n\n\nResearching PMD results...");
            System.out.println(">> BLOB:");
            System.out.println(pmdBlob);

            System.out.println(">> DATA CLASS:");
            System.out.println(pmdDataClass);

            System.out.println(">> LONG METHOD:");
            System.out.println(pmdLongMethod);

        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
            System.out.println(e.toString());
        }
    }

}
