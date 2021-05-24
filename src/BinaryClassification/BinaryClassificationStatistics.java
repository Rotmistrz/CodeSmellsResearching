package BinaryClassification;

public class BinaryClassificationStatistics {
    private int truePositives;
    private int falseNegatives;
    private int falsePositives;
    private int trueNegatives;

    public BinaryClassificationStatistics() {
        this.truePositives = 0;
        this.falseNegatives = 0;
        this.falsePositives = 0;
        this.trueNegatives = 0;
    }

    public int increaseTruePositives() {
        this.truePositives++;

        return this.truePositives;
    }

    public int increaseFalseNegatives() {
        this.falseNegatives++;

        return this.falseNegatives;
    }

    public int increaseFalsePositives() {
        this.falsePositives++;

        return this.falsePositives;
    }

    public int increaseTrueNegatives() {
        this.trueNegatives++;

        return this.trueNegatives;
    }

    public int getTruePositives() {
        return this.truePositives;
    }

    public int getFalseNegatives() {
        return this.falseNegatives;
    }

    public int getFalsePositives() {
        return this.falsePositives;
    }

    public int getTrueNegatives() {
        return this.trueNegatives;
    }

    public int increase(BinaryClassificationResult result) {
        if (result == BinaryClassificationResult.TRUE_POSITIVE) {
            increaseTruePositives();

            return getTruePositives();
        } else if (result == BinaryClassificationResult.TRUE_NEGATIVE) {
            increaseTrueNegatives();

            return getTrueNegatives();
        } else if (result == BinaryClassificationResult.FALSE_POSITIVE) {
            increaseFalsePositives();

            return getFalsePositives();
        } else {
            increaseFalseNegatives();

            return getFalseNegatives();
        }
    }

    public String toString() {
        return "TP: " + getTruePositives() + "\nTN: " + getTrueNegatives() + "\nFP: " + getFalsePositives() + "\nFN: " + getFalseNegatives();
    }
}
