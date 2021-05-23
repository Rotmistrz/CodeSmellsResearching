package PMD.Smells;

import CodeSmells.CodeSmellName;

public class PMDCodeSmellName {
    public static final String EXCESSIVE_METHOD_LENGTH = "ExcessiveMethodLength";

    public static final String EXCESSIVE_CLASS_LENGTH = "ExcessiveClassLength";
    public static final String GOD_CLASS = "GodClass";

    public static final String DATA_CLASS = "DataClass";

    public static int getStandardCodeSmell(String pmdSmell) {
        if (pmdSmell.equals(GOD_CLASS)) {
            return CodeSmellName.BLOB;
        } else if (pmdSmell.equals(EXCESSIVE_CLASS_LENGTH)) {
            return CodeSmellName.BLOB;
        } else if (pmdSmell.equals(EXCESSIVE_METHOD_LENGTH)) {
            return CodeSmellName.LONG_METHOD;
        } else if (pmdSmell.equals(DATA_CLASS)) {
            return CodeSmellName.DATA_CLASS;
        } else {
            return CodeSmellName.UNDEFINED;
        }
    }
}
