package DesigniteJava;

import DesigniteJava.Smells.DesigniteCodeSmellName;
import DesigniteJava.Smells.DesigniteSmell;
import CodeSmells.SmellShortInfo;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.HashMap;
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

    public List<DesigniteSmell> prepareResults(Reader reader, Class clazz) throws FileNotFoundException {
        return new CsvToBeanBuilder(reader).withType(clazz).withSeparator(this.getCsvSeparator()).build().parse();
    }

    public HashMap<String, SmellShortInfo> parseSmells(Reader reader, Class clazz, int[] smells) throws FileNotFoundException {
        List<DesigniteSmell> raw = this.prepareResults(reader, clazz);

        HashMap<String, SmellShortInfo> result = new HashMap<>();

        String currentID;
        int smellCode;

        for (DesigniteSmell smell : raw) {
            smellCode = DesigniteCodeSmellName.getStandardCodeSmell(smell.getSmell());

            if (ArrayUtils.contains(smells, smellCode)) {
                currentID = smell.getComponentID();

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
