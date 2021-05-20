package DesigniteJava;

import CodeSmells.CodeSmell;
import DesigniteJava.Smells.ImplementationSmell;
import DesigniteJava.Smells.SmellShortInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DesigniteAnalyzerProgram {
    public static void main(String[] args) {
        String resourceDir = "./results/designite-java/raw-output";

        DesigniteResultParser parser = new DesigniteResultParser(resourceDir, ',');

        try {
            int[] wantedSmells = { CodeSmell.LONG_METHOD };

            HashMap<String, SmellShortInfo> implementationSmells = parser.parseImplementationSmells("ImplementationSmells.csv", wantedSmells);

            Iterator it = implementationSmells.entrySet().iterator();
            SmellShortInfo smellInfo;

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                smellInfo = (SmellShortInfo) pair.getValue();

                System.out.println(smellInfo.getComponent() + ": " + smellInfo.smellsIntoString());
            }
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage() + " " + e.getStackTrace());
        }
    }
}
