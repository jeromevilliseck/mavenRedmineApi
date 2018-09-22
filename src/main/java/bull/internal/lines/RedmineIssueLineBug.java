/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.lines;

/**
 *
 * @author Bull/Atos
 */
public class RedmineIssueLineBug extends RedmineIssueLine {
    /*Members*/
    private String status;
    private String project;
    private String version;
    private String impact;

    /*Constructors*/
    public RedmineIssueLineBug(final String id, final String subject, final String status, final String createdOn, final
    String project, final String version, final String impact) {
        super(id, subject, createdOn);
        this.status = status;
        this.project = project;
        this.version = version;
        this.impact = impact;
    }

    /*Getters*/
    public String getId() { return id; }
    public String getSubject() { return subject; }
    public String getStatus() { return status; }
    public String getCreatedOn() { return createdOn; }
    public String getProject() { return project; }
    public String getVersion() { return version; }
    public String getImpact() { return impact; }
}
