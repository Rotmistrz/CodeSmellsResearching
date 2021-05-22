package DesigniteJava;

import DesigniteJava.Smells.DesigniteCodeSmell;
import DesigniteJava.Smells.ImplementationSmell;
import DesigniteJava.Smells.Smell;
import DesigniteJava.Smells.SmellShortInfo;
import MLCQ.CodeReview;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DesigniteResultParser {
    private String dir;
    private char csvSeparator;

    public DesigniteResultParser(String dir, char csvSeparator) {
        this.dir = dir;
        this.csvSeparator = csvSeparator;
    }

    public char getCsvSeparator() {
        return this.csvSeparator;
    }

    public String getDir() {
        return this.dir;
    }

    public String getPath(String filename) {
        return this.getDir() + "/" + filename;
    }

    public List<ImplementationSmell> prepareResults(Reader reader) throws FileNotFoundException {
        return new CsvToBeanBuilder(reader).withType(ImplementationSmell.class).withSeparator(this.getCsvSeparator()).build().parse();
    }

    public HashMap<String, SmellShortInfo> parseImplementationSmells(Reader reader, int[] smells) throws FileNotFoundException {
        List<ImplementationSmell> raw = this.prepareResults(reader);

        HashMap<String, SmellShortInfo> result = new HashMap<>();

        String currentID;
        int smellCode;

        for (ImplementationSmell smell : raw) {
            smellCode = DesigniteCodeSmell.getStandardCodeSmell(smell.implementationSmell);

            if (ArrayUtils.contains(smells, smellCode)) {
                currentID = smell.packageName + "." + smell.typeName + "#" + smell.methodName;

                SmellShortInfo smellInfo;

                smellInfo = result.get(currentID);

                if (smellInfo == null) {
                    smellInfo = new SmellShortInfo(currentID);

                    result.put(currentID, smellInfo);
                }

                smellInfo.addSmell(smellCode);
            }
        }

        return result;
    }
}
