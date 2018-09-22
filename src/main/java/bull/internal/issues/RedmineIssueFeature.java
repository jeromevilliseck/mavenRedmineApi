/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.issues;

import java.util.Date;

/**
 * [Enter type description here].
 *
 * @author Bull/Atos
 */
public class RedmineIssueFeature {
    /*Members*/
    private int id;
    private String subject;
    private Date createdOn;
    private Float initialEstimatedTime;
    private Float getEstimatedHours;
    private Float getSpentHours;

    /*Constructors*/
    RedmineIssueFeature(final int id, final String subject, final Date createdOn, final Float initialEstimatedTime
            , final Float getEstimatedHours, final Float getSpentHours) {
        this.id = id;
        this.subject = subject;
        this.createdOn = createdOn;
        this.initialEstimatedTime = initialEstimatedTime;
        this.getEstimatedHours = getEstimatedHours;
        this.getSpentHours = getSpentHours;
    }
}
