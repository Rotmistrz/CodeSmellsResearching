package PMD;

import DesigniteJava.Smells.DesigniteCodeSmellName;
import DesigniteJava.Smells.DesigniteSmell;
import CodeSmells.SmellShortInfo;
import PMD.Smells.PMDCodeSmellName;
import PMD.Smells.PMDSmell;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.lang3.ArrayUtils;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

public class PMDResultParser {
    public char getCsvSeparator() {
        return ',';
    }

    public List<PMDSmell> prepareResults(Reader reader) throws FileNotFoundException {
        return new CsvToBeanBuilder(reader).withType(PMDSmell.class).withSeparator(this.getCsvSeparator()).build().parse();
    }

    public HashMap<String, SmellShortInfo> parseSmells(Reader reader, int[] smells) throws FileNotFoundException {
        List<PMDSmell> raw = this.prepareResults(reader);

        HashMap<String, SmellShortInfo> result = new HashMap<>();

        String currentID;
        int smellCode;

        for (PMDSmell smell : raw) {
            smellCode = PMDCodeSmellName.getStandardCodeSmell(smell.getSmell());

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
