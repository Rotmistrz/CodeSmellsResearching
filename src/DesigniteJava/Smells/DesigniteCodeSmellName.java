package DesigniteJava.Smells;

import CodeSmells.CodeSmellName;

public class DesigniteCodeSmellName {
    public static final String GOD_COMPONENT = "God Component";
    public static final String FEATURE_CONCENTRATION = "Feature Concentration";

    public static final String LONG_METHOD = "Long Method";
    public static final String COMPLEX_METHOD = "Complex Method";

    public static final String FEATURE_ENVY = "Feature Envy";

    public static int getStandardCodeSmell(String designiteSmell) {
        if (designiteSmell.equals(GOD_COMPONENT)) {
            return CodeSmellName.BLOB;
        } else if (designiteSmell.equals(FEATURE_CONCENTRATION)) {
            return CodeSmellName.BLOB;
        } else if (designiteSmell.equals(LONG_METHOD)) {
            return CodeSmellName.LONG_METHOD;
        } else if (designiteSmell.equals(COMPLEX_METHOD)) {
            return CodeSmellName.LONG_METHOD;
        } else if (designiteSmell.equals(FEATURE_ENVY)) {
            return CodeSmellName.FEATURE_ENVY;
        } else {
            return CodeSmellName.UNDEFINED;
        }
    }
}
