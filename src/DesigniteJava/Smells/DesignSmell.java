package DesigniteJava.Smells;

import com.opencsv.bean.CsvBindByName;

public class DesignSmell {

    public DesignSmell() {

    }

    @CsvBindByName(column = "Project Name")
    public String projectName;

    @CsvBindByName(column = "Package Name")
    public String packageName;

    @CsvBindByName(column = "Type Name")
    public String typeName;

    @CsvBindByName(column = "Method Name")
    public String methodName;

    @CsvBindByName(column = "Implementation Smell")
    public String implementationSmell;

    @CsvBindByName(column = "Cause of the Smell")
    public String cause;

}
