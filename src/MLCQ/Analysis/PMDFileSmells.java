package MLCQ.Analysis;

import BinaryClassification.BinaryClassificationResult;
import MLCQ.CodeReview;
import MLCQ.Smells.MLCQCodeSmell;
import PMD.Smells.PMDCodeSmellName;
import PMD.Smells.PMDSmell;

import java.util.LinkedList;

public class PMDFileSmells {
    private String filename;
    private LinkedList<PMDSmell> smells;

    public PMDFileSmells(String filename) {
        this.filename = filename;

        this.smells = new LinkedList<>();
    }

    public PMDFileSmells addSmell(PMDSmell smell) {
        this.smells.add(smell);

        return this;
    }

    public LinkedList<PMDSmell> getSmells() {
        return this.smells;
    }

    public BinaryClassificationResult checkMLCQReview(CodeReview review) {
        LinkedList<PMDSmell> pmdSmell = this.smells;

        int stdSmellName = MLCQCodeSmell.getStandardCodeSmell(review.codeSmell);

        System.out.println("MLCQ Code Smell: " + review.codeSmell + " | Severity: " + review.severity + " | Start line: " + review.startLine + " | End line: " + review.endLine);

        for (PMDSmell pmdSmellDetails : pmdSmell) {
            System.out.println("PMD Smell: " + pmdSmellDetails.getSmell() + " | Line: " + pmdSmellDetails.line);
        }

        if (review.severity.equals(MLCQCodeSmell.NONE)) {
            if (pmdSmell != null) {
                for (PMDSmell pmdSmellDetails : pmdSmell) {
                    if (pmdSmellDetails.isSmelling(stdSmellName) && review.isLineInRange(pmdSmellDetails.line)) {
                        return BinaryClassificationResult.FALSE_POSITIVE;
                    }
                }

                return BinaryClassificationResult.TRUE_NEGATIVE;
            } else {
                return BinaryClassificationResult.TRUE_NEGATIVE;
            }
        } else {
            if (pmdSmell != null) {
                for (PMDSmell pmdSmellDetails : pmdSmell) {
                    if (pmdSmellDetails.isSmelling(stdSmellName) && review.isLineInRange(pmdSmellDetails.line)) {
                        return BinaryClassificationResult.TRUE_POSITIVE;
                    }
                }

                return BinaryClassificationResult.FALSE_NEGATIVE;
            } else {
                return BinaryClassificationResult.FALSE_NEGATIVE;
            }
        }
    }
}
