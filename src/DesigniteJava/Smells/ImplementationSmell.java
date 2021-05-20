package DesigniteJava.Smells;

import com.opencsv.bean.CsvBindByName;

public class ImplementationSmell {

    public ImplementationSmell() {

    }

//    public ImplementationSmell(String projectName, String packageName, String typeName, String methodName, String implementationSmell, String cause) {
//        this.projectName = projectName;
//        this.packageName = packageName;
//        this.typeName = typeName;
//        this.methodName = methodName;
//        this.implementationSmell = implementationSmell;
//        this.cause = cause;
//    }

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
