package DesigniteJava.Smells;

import com.opencsv.bean.CsvBindByName;

abstract public class FunctionSmell extends DesigniteSmell {
    @CsvBindByName(column = "Method Name")
    public String methodName;

    @Override
    public String getComponentID() {
        return packageName + "." + typeName + "#" + methodName;
    }
}
