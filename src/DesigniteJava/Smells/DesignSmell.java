package DesigniteJava.Smells;

import com.opencsv.bean.CsvBindByName;

public class DesignSmell extends FunctionSmell {

    public DesignSmell() {

    }

    @CsvBindByName(column = "Design Smell")
    public String designSmell;


    @Override
    public String getSmell() {
        return designSmell;
    }
}
