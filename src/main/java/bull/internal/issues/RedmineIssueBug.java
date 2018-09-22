/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.issues;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;

/**
 * A class which contains a single Issue only.
 * @author Bull
 */
public class RedmineIssueBug {
    /*Attributes*/
    private int id;
    private String subject;
    private Date createdOn;
    private String statusName;
    private String detectionVersion;
    private String impactType;
    private String partOfTheProject;
    private SortedSet<RedmineIssueBugStatus> listStatusByIssues;

    /*Constructors*/
    public RedmineIssueBug() {
    }

    /**
     * Builds a new Issue.
     * @param id issue number.
     * @param subject subject of issue.
     * @param createdOn issue creation date.
     * @param statusName current status of the issue.
     * @param detectionVersion software version in which the bug was detected
     * @param impactType impact of the issue (if it is blocking or not)
     * @param partOfTheProject project to which the issue belongs (sics, laffic, pte-contact)
     * @param listStatusByIssues Object containing a list sorted by date. This list is the history of the current issue.
     */
    public RedmineIssueBug(final int id, final String subject, final Date createdOn, final String statusName,
            final String detectionVersion, final String impactType, final String partOfTheProject,
            final SortedSet<RedmineIssueBugStatus> listStatusByIssues){
        this.id = id;
        this.subject = subject;
        this.createdOn = createdOn;
        this.statusName = statusName;
        this.detectionVersion = detectionVersion;
        this.impactType = impactType;
        this.partOfTheProject = partOfTheProject;
        this.listStatusByIssues = listStatusByIssues;
    }

    /*Getters*/
    public int getId() {return id;}
    public String getSubject() {return subject;}
    public String getStatusName() {
        return statusName;
    }
    public String getPartOfTheProject() {
        return partOfTheProject;
    }
    public SortedSet<RedmineIssueBugStatus> getListStatusByIssues() {return listStatusByIssues;}
    public String getDetectionVersion() {
        return detectionVersion;
    }
    public String getImpactType() {
        return impactType;
    }

    /*Setters*/
    public void setId(final int id) {this.id = id;}
    public void setSubject(final String subject) {this.subject = subject;}
    public void setCreatedOn(final Date createdOn) {this.createdOn = createdOn;}
    public void setStatusName(final String statusName) {this.statusName = statusName;}
    public void setListStatusByIssues(final SortedSet<RedmineIssueBugStatus> listStatusByIssues) { this.listStatusByIssues = listStatusByIssues;}

    /*Methods*/
    /**
     * returns the date legibly (human) and formatted for spreadsheets.
     * @return date in String format
     */
    public String getCreatedOnDateFormatted(){
        SimpleDateFormat formatTool = new SimpleDateFormat("yyyy-MM-dd");
        return formatTool.format(this.createdOn);
    }

    /**
     * Controls whether the issue has a history or not.
     * @return boolean, true if the issue has a history.
     */
    public boolean checkIfListStatusByIssuesIsEmpty(){
        return this.listStatusByIssues.isEmpty();
    }

    /**
     * Controls position of one item of the current issue history list.
     * @param redmineIssueBugStatus the current issue history sorted list.
     * @return boolean, true if item tested is the first element of this list.
     */
    public Boolean checkIfFirstRedmineStatusElement(RedmineIssueBugStatus redmineIssueBugStatus){
        return redmineIssueBugStatus.equals(this.listStatusByIssues.first());
    }

    @Override
    public String toString() {
        return "RedmineIssueBug{" + "id=" + id +
                ", subject='" + subject + '\'' +
                ", createdOn=" + createdOn +
                ", statusName='" + statusName + '\'' +
                ", listStatusByIssues=" + listStatusByIssues +
                '}';
    }
}
