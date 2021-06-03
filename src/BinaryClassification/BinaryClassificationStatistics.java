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

    public double getRecall() {
        return ((double) getTruePositives() / ((double) getTruePositives() + (double) getFalseNegatives()));
    }

    public double getSpecificity() {
        return ((double) getTrueNegatives() / ((double) getFalsePositives() + (double) getTrueNegatives()));
    }

    public double getPrecision() {
        return ((double) getTruePositives() / ((double) getTruePositives() + (double) getFalsePositives()));
    }

    public double getAccuracy() {
        return (((double) getTruePositives() + (double) getTrueNegatives()) / ((double) getTruePositives() + (double) getTrueNegatives() + (double) getFalsePositives() + (double) getFalseNegatives()));
    }

    public double getMCC() {
        return ((double) ((getTruePositives() * getTrueNegatives()) - (getFalsePositives() * getFalseNegatives()))) / (Math.sqrt((double) (getTruePositives() + getFalsePositives()) * (double) (getTruePositives() + getFalseNegatives()) * (double) (getTrueNegatives() + getFalsePositives()) * (double) (getTrueNegatives() + getFalseNegatives())));
    }

    public String toString() {
        String result = "TP: " + getTruePositives() + "\nTN: " + getTrueNegatives() + "\nFP: " + getFalsePositives() + "\nFN: " + getFalseNegatives() +"\n";
        result += "\nRecall: " + getRecall();
        result += "\nSpecificity: " + getSpecificity();
        result += "\nPrecision: " + getPrecision();
        result += "\nAccuracy: " + getAccuracy();
        result += "\nMCC: " + getMCC();

        return result;
    }
}
