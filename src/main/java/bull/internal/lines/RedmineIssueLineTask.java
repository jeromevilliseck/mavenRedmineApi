/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.lines;

/**
 * [Enter type description here].
 *
 * @author Bull/Atos
 */
public class RedmineIssueLineTask extends RedmineIssueLine {
    /*Members*/
    private String initialEstimatedTime;
    private String estimatedHoursToBeDone;
    private String spentHours;

    /*Constructors*/
    public RedmineIssueLineTask(final String id, final String subject, final String createdOn, final String
            initialEstimatedTime, final String estimatedHoursToBeDone, final String spentHours) {
        super(id, subject, createdOn);
        this.initialEstimatedTime = initialEstimatedTime;
        this.estimatedHoursToBeDone = estimatedHoursToBeDone;
        this.spentHours = spentHours;
    }

    /*Getters*/
    public String getId() { return id; }
    public String getSubject() { return subject; }
    public String getCreatedOn() { return createdOn; }
    public String getInitialEstimatedTime() { return initialEstimatedTime; }
    public String getEstimatedHoursToBeDone() { return estimatedHoursToBeDone; }
    public String getSpentHours() { return spentHours; }
}
