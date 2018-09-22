/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import bull.exception.CriticityLevelEnum;
import bull.exception.MyRuntimeException;

import bull.logger.Logger;
import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Journal;
import com.taskadapter.redmineapi.bean.JournalDetail;
import com.taskadapter.redmineapi.internal.ResultsWrapper;

import bull.internal.issues.RedmineIssueBug;
import bull.internal.issues.RedmineIssueBugStatus;
import bull.internal.issues.RedmineIssueFeature;
import bull.internal.lines.RedmineIssueLineBug;

/**
 * [A class which PUT in an object previously created some Issue lines with parameters specified, in algorithm].
 * @author Bull
 */
public class RedmineUtils {
    /*Members*/
    static final String STATUS_ID = "status_id";

    /*Constructor*/
    private RedmineUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * @implSpec NOT USED IN THE PROJECT -> FOR TESTS ONLY
     * @implNote return an issue of the unique Redmine system as an array of 4 elements with bug ID, subject, creation date,
     * status name
     * @param issueManager the instantiated connector in the main class
     * @param id the identifier of the issue that we want to get
     * @return a RedmineIssueBug object with parameters
     */
    public static RedmineIssueBug setIssue(IssueManager issueManager, int id){
        try {
            Issue localIssue = issueManager.getIssueById(id, Include.journals);
            RedmineIssueBug singleIssueReceived = new RedmineIssueBug();

            //Obtenir la création de l'ISSUE, pas son journal
            Logger.getInstance().info(localIssue.getSubject());
            Logger.getInstance().info(localIssue.getCreatedOn());
            Logger.getInstance().info(localIssue.getTracker().getId());

            //Obtenir le journal de l'ISSUE
            Collection<Journal> journals = localIssue.getJournals();
            Logger.getInstance().info("\u001B[31m" + "Objet Collection<Journal>\n" + journals.toString() + "\n");//DISPLAY journals
            Logger.getInstance().info(journals.iterator().next().getCreatedOn().toString());

            //Creer une liste qui va lister les differents status de l'issue -> vide a ce stade
            SortedSet<RedmineIssueBugStatus> statusList = new TreeSet<>();
            Logger.getInstance().info("\t" + "\u001B[32m" + "Objet SortedList<RedmineIssueBugStatus>\n" + "\t" + statusList.toString() + "\n"); //DISPLAY statusList

            //Boucler
            for(Iterator objetIterator = journals.iterator(); objetIterator.hasNext();){
                Journal journal = (Journal) objetIterator.next();
                Logger.getInstance().info("\t" + "\t" + "\u001B[34m" + "Objet Journal\n" + "\t" + "\t" + journal.toString() + "\n");//DISPLAY objectIterator
                for (JournalDetail journalDetail : journal.getDetails()) {
                    if(journalDetail.getName().equals(STATUS_ID)) {
                        statusList.add(new RedmineIssueBugStatus(journalDetail.getNewValue(), journalDetail.getOldValue(), journal.getCreatedOn()));
                    }
                }
            }
            Logger.getInstance().info("\u001B[7m");
            Logger.getInstance().info(statusList.toString());
            /*END TEST*/

            singleIssueReceived.setId(localIssue.getId());
            singleIssueReceived.setCreatedOn(localIssue.getStartDate());
            singleIssueReceived.setSubject(localIssue.getSubject());
            singleIssueReceived.setStatusName(localIssue.getStatusName());
            singleIssueReceived.setListStatusByIssues(statusList);

            return singleIssueReceived;
        } catch (RedmineException e) {
            Logger.getInstance().fatal("Unable to recover Issues, reason : ", e);
        }   return null;
    }

    /**
     * [Get a List of RedmineIssueBug objects.]
     * @param issueManager the instantiated connector in the main class.
     * @throws RedmineException A problem with the API if there is an exception.
     */
    public static void setMultipleBugIssuesWithTheirStatus(IssueManager issueManager, float offsetParameter,
            int localSize, List<RedmineIssueBug> redmineIssueBugList, String projectKey)
            throws RedmineException {

        DecimalFormat df = new DecimalFormat("##.##"); //DISPLAY
        df.setRoundingMode(RoundingMode.DOWN); //DISPLAY

        if(offsetParameter != 0) { //DISPLAY
            Logger.getInstance().info("Data processed (" + projectKey + ") : " + df.format(offsetParameter / localSize * 100) + "%");
        }

        final Map<String, String> params = new HashMap<>();
        params.put("project_id", projectKey);
        params.put(STATUS_ID, "*");
        params.put("tracker_id", "1");
        params.put("offset", Float.toString(offsetParameter));
        params.put("limit", "100");
        params.put("include", Include.journals.toString());

        ResultsWrapper<Issue> issueWrapper = issueManager.getIssues(params);

        List<Issue> issueList = issueWrapper.getResults();

        for (Issue issue : issueList) {
            String detectionVersion = null;
            String impactType = null;

            String issueSubjectNormalized = issue.getSubject().replace(";", "");

            if(issue.getCustomFieldById(8) != null){
                detectionVersion = issue.getCustomFieldById(8).getValue();
            }

            if(issue.getCustomFieldById(12) != null){
                impactType = issue.getCustomFieldById(12).getValue();
            }

            Issue localIssue = issueManager.getIssueById(issue.getId(), Include.journals);

            Collection<Journal> journals = localIssue.getJournals();
            SortedSet<RedmineIssueBugStatus> statusList = new TreeSet<>();
            for (Journal journal : journals) {
                for (JournalDetail journalDetail : journal.getDetails()) {
                    if (journalDetail.getName().equals(STATUS_ID)) {
                        statusList.add(new RedmineIssueBugStatus(journalDetail.getNewValue(),
                                journalDetail.getOldValue(), journal.getCreatedOn()));
                    }
                }
            }

            redmineIssueBugList.add(new RedmineIssueBug(issue.getId(), issueSubjectNormalized, issue.getCreatedOn(),
                    issue.getStatusName(), detectionVersion, impactType, projectKey, statusList));
        }
    }

    public static void setMultipleFeatureIssuesWithTheirStatus(IssueManager issueManager, float offsetParameter,
            int localSize, List<RedmineIssueFeature> redmineIssueFeatureList, String projectKey)
            throws RedmineException {

        DecimalFormat df = new DecimalFormat("##.##"); //DISPLAY
        df.setRoundingMode(RoundingMode.DOWN); //DISPLAY

        if(offsetParameter != 0) { //DISPLAY
            Logger.getInstance().info("Data processed (" + projectKey + ") : " + df.format(offsetParameter / localSize * 100) + "%");
        }

        final Map<String, String> params = new HashMap<>();
        params.put("project_id", projectKey);
        params.put(STATUS_ID, "*");
        params.put("tracker_id", "4");
        params.put("offset", Float.toString(offsetParameter));
        params.put("limit", "100");
        params.put("include", Include.journals.toString());

        ResultsWrapper<Issue> issueWrapper = issueManager.getIssues(params);

        List<Issue> issueList = issueWrapper.getResults();

        for (Issue issue : issueList) {

            String issueSubjectNormalized = issue.getSubject().replace(";", "");

            Issue localIssue = issueManager.getIssueById(issue.getId(), Include.children);

            localIssue.getId(); //INTEGER
            localIssue.getSubject(); //STRING
            localIssue.getCreatedOn(); //DATE

            Float initialEstimatedTime;
            Collection<CustomField> customFields = localIssue.getCustomFields();
            for(CustomField field : customFields){
                if(field.getId().equals(15)){
                    initialEstimatedTime = Float.valueOf(field.getValue()); //FLOAT
                }
            }

            localIssue.getEstimatedHours(); //FLOAT
            localIssue.getSpentHours(); //FLOAT
        }
    }


    public static void placeRedmineIssuesBugWithAllStatusInLineList(List<RedmineIssueBug> redmineIssueBugList,
                                                                    List<RedmineIssueLineBug> redmineIssueLineBugList){
        for(RedmineIssueBug objectWrote : redmineIssueBugList){

            /*If the Issue hasn't got a Journal, get only One line for this issue*/
            if(objectWrote.checkIfListStatusByIssuesIsEmpty()){
                redmineIssueLineBugList.add(new RedmineIssueLineBug(
                        String.valueOf(objectWrote.getId()),
                        objectWrote.getSubject(),
                        objectWrote.getStatusName(),
                        objectWrote.getCreatedOnDateFormatted(),
                        objectWrote.getDetectionVersion(),
                        objectWrote.getImpactType(),
                        objectWrote.getPartOfTheProject()
                ));
            }

            for(RedmineIssueBugStatus objectStatusWrote : objectWrote.getListStatusByIssues()){
                /*If it's the first Journal element (first line, oldest line)*/
                if(objectWrote.checkIfFirstRedmineStatusElement(objectStatusWrote)){
                    redmineIssueLineBugList.add(new RedmineIssueLineBug(
                            String.valueOf(objectWrote.getId()),
                            objectWrote.getSubject(),
                            objectStatusWrote.getStatusOldNumber(),
                            objectWrote.getCreatedOnDateFormatted(),
                            "",
                            "",
                            objectWrote.getPartOfTheProject()
                    ));
                }

                /*If it is not the first Journal element (all other lines)*/
                redmineIssueLineBugList.add(new RedmineIssueLineBug(
                        String.valueOf(objectWrote.getId()),
                        objectWrote.getSubject(),
                        objectStatusWrote.getStatusNewNumber(),
                        objectStatusWrote.getStatusDateFormatted(),
                        "",
                        "",
                        objectWrote.getPartOfTheProject()
                ));
            }
        }
    }

    public static void placeRedmineIssuesWithOnlyLastStatusByDateInLineList(List<RedmineIssueBug> redmineIssueBugList,
            List<RedmineIssueLineBug> redmineIssueLineBugList){
        for (RedmineIssueBug issueObject : redmineIssueBugList) {

            /*If the history list is not empty*/
            if(!(issueObject.checkIfListStatusByIssuesIsEmpty())){

                /*variable used to remove lines with the same date for one Issue*/
                String issueDateBuffer = issueObject.getCreatedOnDateFormatted(); //BUFFER

                redmineIssueLineBugList.add(new RedmineIssueLineBug(
                        String.valueOf(issueObject.getId()),
                        issueObject.getSubject(),
                        issueObject.getListStatusByIssues().first().getStatusOldNumber(),
                        issueObject.getCreatedOnDateFormatted(),
                        issueObject.getDetectionVersion(),
                        issueObject.getImpactType(),
                        issueObject.getPartOfTheProject()
                ));

                for(RedmineIssueBugStatus issueHistoryObject : issueObject.getListStatusByIssues()){

                    /*if the date of the next data line is different from the previous one*/
                    if(!(issueDateBuffer.equals(issueHistoryObject.getStatusDateFormatted()))) {
                        redmineIssueLineBugList.add(new RedmineIssueLineBug(
                                String.valueOf(issueObject.getId()),
                                issueObject.getSubject(),
                                issueHistoryObject.getStatusNewNumber(),
                                issueHistoryObject.getStatusDateFormatted(),
                                "",
                                "",
                                issueObject.getPartOfTheProject()
                        ));
                    }

                    issueDateBuffer = issueHistoryObject.getStatusDateFormatted(); //BUFFER
                }
            }else{ /*If the history list is empty for an issue and there is only one line*/
                redmineIssueLineBugList.add(new RedmineIssueLineBug(
                        String.valueOf(issueObject.getId()),
                        issueObject.getSubject(),
                        issueObject.getStatusName(),
                        issueObject.getCreatedOnDateFormatted(),
                        issueObject.getDetectionVersion(),
                        issueObject.getImpactType(),
                        issueObject.getPartOfTheProject()
                ));
            }
        }
    }

    public static void placeRedmineIssuesWithOnlyReopenedStatusInLineList(List<RedmineIssueBug> redmineIssueBugList,
            List<RedmineIssueLineBug> redmineIssueLineBugList){
        for (RedmineIssueBug issueObject : redmineIssueBugList) {

            /*If the history list is not empty*/
            if(!(issueObject.checkIfListStatusByIssuesIsEmpty())){
                for(RedmineIssueBugStatus issueHistoryObject : issueObject.getListStatusByIssues()){
                    if(issueHistoryObject.checkIfStatusIsNewFromIntegrated()){
                        redmineIssueLineBugList.add(new RedmineIssueLineBug(
                                String.valueOf(issueObject.getId()),
                                issueObject.getSubject(),
                                issueHistoryObject.getStatusNewNumber(),
                                issueHistoryObject.getStatusDateFormatted(),
                                issueObject.getDetectionVersion(),
                                issueObject.getImpactType(),
                                issueObject.getPartOfTheProject()
                        ));
                    }
                }
            }
        }
    }

    /**
     * [Display the total number of Issues.]
     * @param issueManager the manager which allow connection with sics redmine here.
     */
    public static void displaySizeOfIssuesList(IssueManager issueManager, String projectKey){
        int countNumberOfIssues = 0;
        try {
            countNumberOfIssues = issueManager.getIssues(projectKey, null, Include.journals).size();
            System.out.println(countNumberOfIssues);
        } catch (RedmineException e) {
            throw new MyRuntimeException(CriticityLevelEnum.WARNING, "This issue number doesn't exist", e);
        }

        Logger.getInstance().info("Number of " + projectKey + " Issues to be processed : " + Integer.toString(countNumberOfIssues));
    }

    /**
     * [Display the total number of tuples (Issues with their status lines).]
     * @param issueManager the manager which allow connection with sics redmine here.
     * @param redmineIssueType equals 1 if the RedmineissueType wanted are Bugs, equals 4 if are the Features
     * @return int, the total number of tuples
     * @throws RedmineException if the Redmine API doesn't work.
     */
    public static int getSizeOfIssuesListWithJournalLines(IssueManager issueManager, String projectKey,
            int redmineIssueType) {
        Logger.getInstance().info("Calculation of the sum of lines to be processed for the project " + projectKey + ", wait...");
        List<Issue> test = null;

        try {
            test = issueManager.getIssues(projectKey, null, Include.journals);
        } catch (RedmineException e) {
            throw new MyRuntimeException(CriticityLevelEnum.WARNING, "This Issue doesn't exist", e);
        }

        int totalNumberOfLines = 0;
        for(Issue issue : test){
            Issue localIssue = null;

            try {
                localIssue = issueManager.getIssueById(issue.getId(), Include.journals);
            } catch (RedmineException e) {
                throw new MyRuntimeException(CriticityLevelEnum.WARNING, "This Issue doesn't exist", e);
            }

            if(localIssue.getTracker().getId() == redmineIssueType) {
                Collection<Journal> journals = localIssue.getJournals();
                totalNumberOfLines += journals.size();
            }
        }

        if(redmineIssueType == 1) {
            Logger.getInstance().info("BUG lines to process : " + Integer.toString(totalNumberOfLines));
        }else if(redmineIssueType == 4){
            Logger.getInstance().info("FEATURES lines to process : " + Integer.toString(totalNumberOfLines));
        }

        return totalNumberOfLines;
    }
}
