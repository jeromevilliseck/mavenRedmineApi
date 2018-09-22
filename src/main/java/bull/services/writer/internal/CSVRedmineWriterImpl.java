/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.services.writer.internal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import bull.internal.utils.FileManager;
import bull.internal.lines.RedmineIssueLineBug;
import bull.services.writer.IndicatorsWriter;

/**
 * Write CSV files.
 * @author Bull/Atos
 */

public class CSVRedmineWriterImpl implements IndicatorsWriter<RedmineIssueLineBug> {

    /**
     * Creates a CSV file writer object then makes it create a file
     * and then fills it using a Normalized list passed as a parameter.
     * @param redmineIssueLineBugList An object that represents a standard data line (one line).
     * @param fileName The name of the output file.
     */
    public void writeIssues(List<RedmineIssueLineBug> redmineIssueLineBugList, String fileName) {
        FileManager target = new FileManager(System.getProperty("user.home")
                + FileManager.getPathOsSeparator(),this.getCurrentDateAndHours() + fileName, "csv");
        target.createNewFile();
        target.writeRedmineLine(redmineIssueLineBugList);
    }

    private String getCurrentDateAndHours(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
