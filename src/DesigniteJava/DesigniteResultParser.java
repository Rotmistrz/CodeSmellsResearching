package DesigniteJava;

import DesigniteJava.Smells.DesigniteCodeSmell;
import DesigniteJava.Smells.ImplementationSmell;
import DesigniteJava.Smells.Smell;
import DesigniteJava.Smells.SmellShortInfo;
import MLCQ.CodeReview;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.ArrayUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

    public List<ImplementationSmell> prepareResults(String pathToSource) throws FileNotFoundException {
        return new CsvToBeanBuilder(new FileReader(pathToSource)).withType(ImplementationSmell.class).withSeparator(this.getCsvSeparator()).build().parse();
    }

    public HashMap<String, SmellShortInfo> parseImplementationSmells(String filename, int[] smells) throws FileNotFoundException {
        List<ImplementationSmell> raw = this.prepareResults(getPath(filename));

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
