package PMD.Smells;

import CodeSmells.CodeSmellName;
import CodeSmells.Smell;
import com.opencsv.bean.CsvBindByName;

public class PMDSmell extends Smell {

    @CsvBindByName(column = "Problem")
    public int problem;

    @CsvBindByName(column = "Package")
    public String packageName;

    @CsvBindByName(column = "File")
    public String file;

    @CsvBindByName(column = "Priority")
    public int priority;

    @CsvBindByName(column = "Line")
    public int line;

    @CsvBindByName(column = "Description")
    public String description;

    @CsvBindByName(column = "Rule set")
    public String ruleSet;

    @CsvBindByName(column = "Rule")
    public String rule;

    @Override
    public String getSmell() {
        return rule;
    }

    @Override
    public String getComponentID() {
        String[] parts = file.split("\\\\");

        String filename = parts[parts.length - 1];

        parts = filename.split("\\.");

        return parts[0];
    }

    public boolean isSmelling(int smellCode) {
        if (smellCode == CodeSmellName.BLOB) {
            return rule.equals(PMDCodeSmellName.EXCESSIVE_CLASS_LENGTH) || rule.equals(PMDCodeSmellName.GOD_CLASS);
        } else if (smellCode == CodeSmellName.LONG_METHOD) {
            return rule.equals(PMDCodeSmellName.EXCESSIVE_METHOD_LENGTH);
        } else if (smellCode == CodeSmellName.DATA_CLASS) {
            return rule.equals(PMDCodeSmellName.DATA_CLASS);
        } else {
            return false;
        }
    }
}
