package MLCQ;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AnalyzerMLCQ {
    private String sourceFile;
    private String logsDir;
    private char csvSeparator;

    public AnalyzerMLCQ(String sourceFile, String logsDir, char csvSeparator) {
        this.sourceFile = sourceFile;
        this.logsDir = logsDir;
        this.csvSeparator = csvSeparator;
    }

    public String getSourceFile() {
        return this.sourceFile;
    }

    public String getLogsDir() {
        return this.logsDir;
    }

    public char getCsvSeparator() {
        return this.csvSeparator;
    }

    public List<CodeReview> prepareReviews(String pathToSource) throws FileNotFoundException {
        return new CsvToBeanBuilder(new FileReader(pathToSource)).withType(CodeReview.class).withSeparator(this.getCsvSeparator()).build().parse();
    }

    public String getSubdirectoryName(CodeReview review) {
        String baseName;

        String[] parts;

        if (review.codeName.indexOf(" ") > 0) {
            parts = review.codeName.split(" ");

            baseName = parts[0];
        } else {
            baseName = review.codeName;
        }

        if (baseName.indexOf("#") > 0) {
            parts = baseName.split("#");

            baseName = parts[0];
        }

        parts = baseName.split("\\.");

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < parts.length && i < 1; i++) {
            result.append(parts[i]);
        }

        return result.toString();
    }

    public List<CodeReview> receiveCodeSamples(String outputDir, List<CodeReview> reviews, int attemptsNumber) throws IOException, InterruptedException {
        Document document;
        Elements lines;
        String sampleOutputPath;
        String packageDirectory;

        HashMap<Integer, String> failures = new HashMap<>();
        Set<Integer> receivedSamples = new HashSet<>();
        LinkedList<String> failureLogs = new LinkedList<>();

        List<CodeReview> reachableReviews = new LinkedList<>();

        int expectedFiles = 0;
        int actualFiles = 0;

        for (CodeReview review : reviews) {
            if (receivedSamples.contains(review.getSampleID())) {
                reachableReviews.add(review);
            } else {
                if (!failures.containsKey(review.getSampleID())) {
                    expectedFiles++;
                }

                document = getDocument(review.getLink(), attemptsNumber);

                if (document != null) {
                    lines = document.select(".type-java .js-file-line");

                    if (lines.size() > 0) {
                        if (lines.size() <= 4000) {
                            packageDirectory = outputDir + "/" + getSubdirectoryName(review);

                            File packageDir = new File(packageDirectory);

                            if (!packageDir.exists()) {
                                packageDir.mkdir();
                            }

                            sampleOutputPath = packageDirectory + "/" + review.getSampleID() + ".java";

                            BufferedWriter writer = new BufferedWriter(new FileWriter(sampleOutputPath, false));

                            for (Element line : lines) {
                                writer.append(line.wholeText() + "\n");
                            }

                            writer.close();

                            receivedSamples.add(review.getSampleID());
                            reachableReviews.add(review);
                            actualFiles++;

                            // console logs
                            System.out.println("Sample " + review.getSampleID() + " saved! " + review.codeName);
                        } else {
                            failures.put(review.getSampleID(), review.getLink());
                            failureLogs.add(review.getReviewID() + ", " + review.getSampleID() + ", " + review.getCodeSmell() + ", " + review.getSeverity() + ", " + review.getLink() + ", too large file to receive");

                            // console logs
                            System.out.println(review.getReviewID() + ", " + review.getSampleID() + ": problem with receiving the code - too large file.");
                        }
                    } else {
                        failures.put(review.getSampleID(), review.getLink());
                        failureLogs.add(review.getReviewID() + ", " + review.getSampleID() + ", " + review.getCodeSmell() + ", " + review.getSeverity() + ", " + review.getLink() + ", too large file to even reach it");

                        // console logs
                        System.out.println(review.getReviewID() + ", " + review.getSampleID() + ": problem with receiving the code - too large file to even reach it.");
                    }
                } else {
                    failures.put(review.getSampleID(), review.getLink());
                    failureLogs.add(review.getReviewID() + ", " + review.getSampleID() + ", " + review.getCodeSmell() + ", " + review.getSeverity() + ", " + review.getLink() + ", 404 Not Found");

                    // console logs
                    System.out.println(review.getReviewID() + ", " + review.getSampleID() + ": problem with receiving the code - 404 Not Found.");
                }
            }
        }

        String logsPath = this.getLogsDir() + "/" + this.getReceivingSamplesLogsFilename();

        BufferedWriter logsWriter = new BufferedWriter(new FileWriter(logsPath, false));

        if (failureLogs.size() > 0) {
            logsWriter.append("Receiving files with warnings: Received " + actualFiles + " of " + expectedFiles + " expected samples.\n");
            logsWriter.append("Below are unreachable samples.\n");

            for (String log : failureLogs) {
                logsWriter.append(log + "\n");
            }
        } else {
            logsWriter.append("Receiving files: All "  + actualFiles + " received successfully.\n");
        }

        logsWriter.close();

        return reachableReviews;
    }

    public static String getReceivingSamplesLogsFilename() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();

        return "logs-receiving-samples-" + formatter.format(date) + ".txt";
    }

    public static String getFinalCodeReviewsFilename() {
        return "final-code-reviews.csv";
    }

    public void saveCodeReviews(String outputDir, List<CodeReview> reviews) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String outputPath = outputDir + "/" + getFinalCodeReviewsFilename();

        Writer writer  = new FileWriter(outputPath);

        StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(this.getCsvSeparator())
                .build();

        sbc.write(reviews);
    }

    public static Document getDocument(String url, int maxAttempts) throws InterruptedException {
        boolean isDownloaded = false;
        int attempt = 0;
        Document result = null;

        while (!isDownloaded && attempt < maxAttempts) {
            try {
                result = Jsoup.connect(url).get();

                return result;
            } catch (IOException e) {
                attempt++;
                TimeUnit.SECONDS.sleep(2);
            }
        }

        return null;
    }
}