package DesigniteJava.Smells;

import com.opencsv.bean.CsvBindByName;

public class ImplementationSmell extends FunctionSmell {

    public ImplementationSmell() {

    }

    @CsvBindByName(column = "Implementation Smell")
    public String implementationSmell;

    @Override
    public String getSmell() {
        return implementationSmell;
    }
}
