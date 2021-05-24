package PMD;

import DesigniteJava.Smells.DesigniteCodeSmellName;
import DesigniteJava.Smells.DesigniteSmell;
import CodeSmells.SmellShortInfo;
import MLCQ.Analysis.PMDFileSmells;
import PMD.Smells.PMDCodeSmellName;
import PMD.Smells.PMDSmell;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.ArrayUtils;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PMDResultParser {
    public char getCsvSeparator() {
        return ',';
    }

    public List<PMDSmell> prepareResults(Reader reader) throws FileNotFoundException {
        return new CsvToBeanBuilder(reader).withType(PMDSmell.class).withSeparator(this.getCsvSeparator()).build().parse();
    }

    public HashMap<String, PMDFileSmells> parseSmells(Reader reader, int[] smells) throws FileNotFoundException {
        List<PMDSmell> raw = this.prepareResults(reader);

        HashMap<String, PMDFileSmells> result = new HashMap<>();

        String currentID;
        int smellCode;

        for (PMDSmell smell : raw) {
            smellCode = PMDCodeSmellName.getStandardCodeSmell(smell.getSmell());

            if (ArrayUtils.contains(smells, smellCode)) {
                currentID = smell.getComponentID();

                PMDFileSmells pmdSmells;

                pmdSmells = result.get(currentID);

                if (pmdSmells == null) {
                    pmdSmells = new PMDFileSmells(currentID);

                    result.put(currentID, pmdSmells);
                }

                pmdSmells.addSmell(smell);
            }
        }

        return result;
    }
}
