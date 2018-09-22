/*
 * Copyright (c) 2018 Bull/Atos.
 * All rights reserved.
 */
package bull.connector;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;

/**
 * Allows connection to SICS project or other through Redmine API.
 * @author Bull
 */
public class RedmineConnection {
    /*Attributes*/
    private RedmineManager mgr;

    /*Enumerations*/
    public enum Project{
        SICS
    }

    /*Constructors*/
    /**
     * Retrieves data via login credentials.
     * @param project enum indicating the global project (sics for here the 3 projects: sics, laffic, pte-contact).
     */
    public RedmineConnection(Project project){
        if (project.equals(Project.SICS)){
            String uri = "RESTRICTED - CONFIDENTIAL";
            String apiAccessKey = "RESTRICTED - CONFIDENTIAL";
            mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
        }
    }

    /*Getters*/
    public IssueManager getIssueManager() { return this.mgr.getIssueManager(); }
}
