package MLCQ;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvBindByName;

public class CodeReview {
    public CodeReview() {

    }

    @CsvBindByName(column = "id")
    public int reviewID;

    @CsvBindByName(column = "reviewer_id")
    public int reviewerID;

    @CsvBindByName(column = "sample_id")
    public int sampleID;

    @CsvBindByName(column = "smell")
    public String codeSmell;

    @CsvBindByName(column = "severity")
    public String severity;

    @CsvBindByName(column = "review_timestamp")
    public String timestamp;

    @CsvBindByName(column = "type")
    public String type;

    @CsvBindByName(column = "code_name")
    public String codeName;

    @CsvBindByName(column = "repository")
    public String repository;

    @CsvBindByName(column = "commit_hash")
    public String commitHash;

    @CsvBindByName(column = "path")
    public String path;

    @CsvBindByName(column = "start_line")
    public int startLine;

    @CsvBindByName(column = "end_line")
    public int endLine;

    @CsvBindByName(column = "link")
    public String link;

    @CsvBindByName(column = "is_from_industry_relevant_project")
    public String isFromIndustryRelevantProject;

    public boolean isLineInRange(int externalLine) {
        return externalLine > startLine && externalLine < endLine;
    }

    public String getCodeName() {
        return this.codeName;
    }

    public String getPreparedCodeName() {
        if (this.codeName.indexOf(" ") > 0) {
            String[] res = this.codeName.split(" ");

            String core = res[0];

            if (core.indexOf("#") > 0) {
                return core;
            } else {
                String[] coreParts = core.split("\\.");
                int classLen = coreParts.length - 1;

                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < classLen; i++) {
                    if (i > 0) {
                        builder.append(".");
                    }

                    builder.append(coreParts[i]);
                }

                builder.append("#").append(coreParts[classLen]);

                return builder.toString();
            }
        } else {
            return this.codeName;
        }
    }

    public String getCommitHash() {
        return this.commitHash;
    }

    public int getEndLine() {
        return this.endLine;
    }

    public int getReviewID() {
        return reviewID;
    }

    public int getReviewerID() {
        return reviewerID;
    }

    public int getSampleID() {
        return sampleID;
    }

    public String getCodeSmell() {
        return codeSmell;
    }

    public String getSeverity() {
        return severity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getRepository() {
        return repository;
    }

    public String getPath() {
        return path;
    }

    public int getStartLine() {
        return startLine;
    }

    public String getLink() {
        return link;
    }

    public String getIsFromIndustryRelevantProject() {
        return isFromIndustryRelevantProject;
    }

    //  getters, setters, toString
}

