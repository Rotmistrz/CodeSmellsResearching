package MLCQ.Researching;

import MLCQ.AnalyzerMLCQ;
import MLCQ.CodeReview;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ResearchingMLCQProgramReceivingSamples {
    public static void main(String[] args) {
        String filename = "./resources/MLCQCodeSmellSamples.csv";
        String resultsDir = "./results/mlcq";

        String resultsCodeDir = resultsDir + "/code";
        String resultsReviewsDir = resultsDir + "/csv";

        try {
            AnalyzerMLCQ analyzer = new AnalyzerMLCQ(filename, "./logs", ';');

            Reader reader = new FileReader(filename);
            BufferedReader buffReader = new BufferedReader(reader);

            List<CodeReview> reviews = analyzer.prepareReviews(buffReader);

            reader.close();
            buffReader.close();

            int expectedFiles = reviews.size();

            List<CodeReview> reachableReviews = analyzer.receiveCodeSamples(resultsCodeDir, reviews, 3);

            int actualFiles = reachableReviews.size();

            if (expectedFiles == actualFiles) {
                System.out.println("Receiving files completed successfully!");
            } else {
                System.out.println("Receiving files failed. Received " + actualFiles + " of " + expectedFiles + " reviews.");
            }

            analyzer.saveCodeReviews(resultsReviewsDir, reachableReviews);
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }
}