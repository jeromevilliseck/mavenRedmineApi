/*
 * Copyright (c) 2018 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import bull.internal.issues.Upsource;
import bull.internal.lines.RedmineIssueLineBug;
import bull.logger.Logger;

/**
 * [Generate CSV file from a List or an object Redmine.]
 * @author Bull/Villiseck
 */
public class FileManager {
    /*Attributes*/
    private String filePath;
    private String fileExtension;
    private String fileName;
    private File file;
    private FileWriter writer;

    /*Constructors*/
    public FileManager(String pathfile, String fileName, String fileExtension){
        this.filePath = pathfile;
        this.fileExtension = fileExtension;
        this.fileName = fileName;
        this.file = new File(pathfile + fileName + "." + fileExtension);
        try {
            this.writer = new FileWriter(new File(pathfile + fileName + "." + fileExtension));
        } catch (IOException e) {
            Logger.getInstance().info("File created : ", e);
        }
    }

    /*Methods*/
    /**
     * [Create a new file for the private class attribute file, assign to private class attribute fileCreated a
     * boolean true if file is created.]
     */
    public void createNewFile(){
        try {
            if(this.file.createNewFile()){
                Logger.getInstance().info("File creation successful");
            }
        } catch (IOException e) {
            Logger.getInstance().fatal("Failure at file creation time, error report below : \n", e);
        }
    }

    public void writeRedmineLine(List<RedmineIssueLineBug> redmineIssueLineBugList) {
        /*BOM UTF-8 (Excel compatibility)*/
        try {
            this.writer.write('\uFEFF');
        } catch (IOException e) {
            Logger.getInstance().fatal("file not created, reason : ", e);
        }

        /*Write lines in CSV File*/
        CSVUtils.writeLine(this.writer, Arrays.asList("ID", "SUJET", "STATUT", "CREE LE", "PROJET", "VERSION", "IMPACT"));

        for (RedmineIssueLineBug issueObject: redmineIssueLineBugList) {
            CSVUtils.writeLine(this.writer, Arrays.asList(issueObject.getId(), issueObject.getSubject(), issueObject.getStatus(),
                    issueObject.getCreatedOn(), issueObject.getProject(), issueObject.getVersion(), issueObject.getImpact()));
        }

        /*Logger message about file created*/
        String varLogger = "Redmine -> " + this.fileName + " file"
                + " in " + this.fileExtension + " format"
                + " created in " + this.filePath
                + ".\nEncoding : " + this.writer.getEncoding() + ".";
        Logger.getInstance().info(varLogger);

        /*Cache cleaning*/
        try {
            this.writer.flush();
        } catch (IOException e) {
            Logger.getInstance().fatal("Flush error, reason : ", e);
        }
    }

    public void writeUpsourceLine(List<Upsource> upsourceList){
        /*BOM UTF-8 (Excel compatibility)*/
        try {
            this.writer.write('\uFEFF');
        } catch (IOException e) {
            Logger.getInstance().fatal("file not created, reason : ", e);
        }

        /*Write lines in CSV File*/
        CSVUtils.writeLine(this.writer, Arrays.asList(
                "PROJECT", "BRANCH", "REVISION ID", "REVISION DATE", "STATE REVIEW", "REVIEW ID", "REVIEW DATE"));

        for (Upsource upsourceObject : upsourceList){
            CSVUtils.writeLine(this.writer, Arrays.asList(upsourceObject.getProjectId(),
                    upsourceObject.getBranch(), upsourceObject.getRevisionId(), upsourceObject.getRevisionDate(),
                    upsourceObject.getState(), upsourceObject.getReviewId(), upsourceObject.getReviewDate()));
        }

        /*Logger message about file created*/
        String varLogger = "Upsource -> " + this.fileName + " file"
                + " in " + this.fileExtension + " format"
                + " created in " + this.filePath
                + ".\nEncoding : " + this.writer.getEncoding() + ".";
        Logger.getInstance().info(varLogger);

        /*Cache cleaning*/
        try {
            this.writer.flush();
        } catch (IOException e) {
            Logger.getInstance().fatal("Flush error, reason : ", e);
        }
    }

    /**
     * [Enable to write the filePath to save the file in the correct format depending on the operating system]
     * @return slash for linux user, blackslash for windows user
     */
    public static String getPathOsSeparator(){
        String userOS = System.getProperty("os.name");
        if(userOS.toLowerCase().contains("windows")){
            return "\\";
        }else{
            return "/";
        }
    }
}


