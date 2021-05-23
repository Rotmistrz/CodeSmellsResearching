package DesigniteJava.Smells;

import CodeSmells.Smell;
import com.opencsv.bean.CsvBindByName;

abstract public class DesigniteSmell extends Smell {
    @CsvBindByName(column = "Project Name")
    public String projectName;

    @CsvBindByName(column = "Package Name")
    public String packageName;

    @CsvBindByName(column = "Type Name")
    public String typeName;

    @CsvBindByName(column = "Cause of the Smell")
    public String cause;
}
