package DesigniteJava;

import DesigniteJava.Smells.DesigniteCodeSmellName;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

public class DesigniteAnalyzerProgram {
    public static boolean smellFits(String smell) {
        return smell.equals(DesigniteCodeSmellName.FEATURE_ENVY);
    }

    public static void main(String[] args) {
        String packageName = "__from-j-to-z";
        String resourceDir = "./results/designite-java/raw-output";

        String fileName = "DesignSmells.csv";

        DesigniteResultParser parser = new DesigniteResultParser(resourceDir, ',');

        try {

            Reader reader = new FileReader(resourceDir + "/" + packageName + "/" + fileName);
            Reader buffReader = new BufferedReader(reader);

            //List<String[]> list = new LinkedList<>();
            CSVReader csvReader = new CSVReader(buffReader);

            CSVWriter writer = new CSVWriter(new FileWriter("./results/designite-java/parsed-output/" + packageName + "/" + fileName));

            String[] line;
            String[] finalLine;
            String[] parts;
            String smell;
            String cause;

            while ((line = csvReader.readNext()) != null) {
                smell = line[3];

                if (smellFits(smell)) {
                    finalLine = new String[line.length + 1];
                    finalLine[0] = line[0];
                    finalLine[1] = line[1];
                    finalLine[2] = line[2];

                    cause = line[4];
                    parts = cause.split(" ");

                    finalLine[3] = parts[9];
                    finalLine[4] = line[3];
                    finalLine[5] = line[4];

                    writer.writeNext(finalLine);

                    System.out.println(finalLine[1] + " " + finalLine[4]);
                }
            }

            reader.close();
            buffReader.close();
            csvReader.close();
            writer.close();

//
//            int[] wantedSmells = { CodeSmell.LONG_METHOD };
//
//            Reader reader = new FileReader(resourceDir + "/" + "org/ImplementationSmells.csv");
//
//            HashMap<String, SmellShortInfo> implementationSmells = parser.parseImplementationSmells(reader, wantedSmells);
//
//            reader.close();
//
//            Iterator it = implementationSmells.entrySet().iterator();
//            SmellShortInfo smellInfo;
//
//            while (it.hasNext()) {
//                Map.Entry pair = (Map.Entry) it.next();
//                smellInfo = (SmellShortInfo) pair.getValue();
//
//                System.out.println(smellInfo.getComponent() + ": " + smellInfo.smellsIntoString());
//            }
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage() + " " + e.getStackTrace());
        }
    }
}
