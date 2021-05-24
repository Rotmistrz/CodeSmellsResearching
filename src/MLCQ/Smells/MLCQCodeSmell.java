package MLCQ.Smells;

import CodeSmells.CodeSmellName;

public class MLCQCodeSmell {
    public static final String LONG_METHOD = "long method";
    public static final String BLOB = "blob";
    public static final String DATA_CLASS = "data class";
    public static final String FEATURE_ENVY = "feature envy";

    public static final String NONE = "none";

    public static int getStandardCodeSmell(String mlcqSmell) {
        if (mlcqSmell.equals(LONG_METHOD)) {
            return CodeSmellName.LONG_METHOD;
        } else if (mlcqSmell.equals(BLOB)) {
            return CodeSmellName.BLOB;
        } else if (mlcqSmell.equals(DATA_CLASS)) {
            return CodeSmellName.DATA_CLASS;
        } else if (mlcqSmell.equals(FEATURE_ENVY)) {
            return CodeSmellName.FEATURE_ENVY;
        } else {
            return CodeSmellName.UNDEFINED;
        }
    }
}
