/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.services.writer.internal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import bull.internal.issues.Upsource;
import bull.internal.lines.RedmineIssueLineBug;
import bull.internal.utils.FileManager;
import bull.services.writer.IndicatorsWriter;

/**
 * [Enter type description here].
 *
 * @author Bull/Atos
 */
public class CSVUpsourceWriterImpl implements IndicatorsWriter<Upsource> {
    public void writeIssues(List<Upsource> upsourceList, String fileName){
        FileManager target = new FileManager(System.getProperty("user.home")
                + FileManager.getPathOsSeparator(), this.getCurrentDateAndHours() + fileName, "csv");
        target.createNewFile();
        target.writeUpsourceLine(upsourceList);
    }

    private String getCurrentDateAndHours(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss ");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
