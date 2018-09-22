/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.issues;

/**
 * Defines a revision (commit).
 *
 * @author Bull/Atos
 */
public class Upsource {
    private String projectId;
    private String branch;
    private String revisionId;
    private String title;
    private String reviewId;
    private String revisionDate;
    private String reviewDate;
    private String reviewUpdateDate;
    private String state;


    /**
     * Creates a revision (commit).
     * state, State of the revision's review (0: None, 1: Opened, 2: Closed).
     * @param projectId Project name of the revision (BMS).
     * @param revisionId ID of the revision (commit).
     * @param revision_Date Date of the revision (commit).
     * @param title Message of the revision (commit).
     */
    public Upsource(final String projectId, final String revisionId, final String revision_Date, final String title) {
        this.projectId = projectId;
        this.revisionId = revisionId;
        this.title = title;
        this.revisionDate = revision_Date;
        this.state = String.valueOf(0);
    }


    /**
     * @return String, ID of the revision.
     */
    public String getRevisionId() {
        return revisionId;
    }

    public String getState() {
        switch(this.state){
            case "1":
                return "opened";
            case "2":
                return "closed";
            default:
                return "no review";
        }
    }

    /**
     * @return String, title of the revision.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return String, ID of the review of the revision.
     */
    public String getReviewId() {
        if (reviewId != null) {
            return reviewId;
        } else {
            return "";
        }
    }

    public void setBranch(final String branch) {
        this.branch = branch;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getBranch() {
        return branch;
    }

    public String getRevisionDate() {
        return revisionDate;
    }

    public String getReviewDate() {
        if (reviewDate != null) {
            return reviewDate;
        } else {
            return "";
        }
    }

    public String getReviewUpdateDate() {
        if (reviewUpdateDate != null) {
            return reviewUpdateDate;
        } else {
            return "";
        }

    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setReviewId(final String review_Id) {
        this.reviewId = review_Id;
    }

    public void setState(final int state) {
        this.state = String.valueOf(state);
    }

    public void setReviewDate(final String review_Date) {
        this.reviewDate = review_Date;
    }

    public void setReviewUpdateDate(final String review_Update_Date) {
        this.reviewUpdateDate = review_Update_Date;
    }

    @Override
    public String toString() {
        return "Upsource{" + "projectId='" + projectId + '\'' +
                ", branch='" + branch + '\'' +
                ", revisionId='" + revisionId + '\'' +
                ", title='" + title + '\'' +
                ", reviewId='" + reviewId + '\'' +
                ", revisionDate=" + revisionDate +
                ", reviewDate=" + reviewDate +
                ", reviewUpdateDate=" + reviewUpdateDate +
                ", state=" + state +
                '}';
    }
}
