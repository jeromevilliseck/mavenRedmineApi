/*
 * Copyright (c) 2016 Bull/Atos.
 * All rights reserved.
 */
package bull.services.extractor.internal;

import java.util.ArrayList;
import java.util.List;

import bull.services.extractor.internal.enums.MethodChoiceEnum;
import com.taskadapter.redmineapi.IssueManager;

import bull.internal.issues.RedmineIssueBug;
import bull.internal.issues.RedmineIssueFeature;
import bull.internal.lines.RedmineIssueLineBug;
import bull.internal.utils.RedmineUtils;

/**
 * [Enter type description here].
 *
 * @author Bull/Atos
 */
public class RedmineExtractImpl {
    //TODO class to delete

    /*Members*/
    private IssueManager issueManager;
    private List<RedmineIssueBug> redmineIssueBugList;
    private List<RedmineIssueFeature> redmineIssueFeatureList;

    private List<RedmineIssueLineBug> redmineIssueLineBugList;

    private String fileName;

    /*Constructor*/
    public RedmineExtractImpl(){
        this.redmineIssueBugList = new ArrayList<>();
        this.redmineIssueFeatureList = new ArrayList<>();
        this.redmineIssueLineBugList = new ArrayList<>();
    }


    public List<RedmineIssueLineBug> sortRedmineIssues(MethodChoiceEnum methodChoiceEnum){
        switch (methodChoiceEnum){
            case ALLSTATUS:
                redmineIssueLineBugList.clear();
                RedmineUtils.placeRedmineIssuesBugWithAllStatusInLineList(this.redmineIssueBugList, this.redmineIssueLineBugList);
                fileName = "redmine_issuesListWithAllStatus";
                break;
            case LASTSTATUS:
                this.redmineIssueLineBugList.clear();
                RedmineUtils.placeRedmineIssuesWithOnlyLastStatusByDateInLineList(this.redmineIssueBugList, this.redmineIssueLineBugList);
                fileName = "redmine_issuesListWithLastStatus";
                break;
            case REOPENED:
                this.redmineIssueLineBugList.clear();
                RedmineUtils.placeRedmineIssuesWithOnlyReopenedStatusInLineList(this.redmineIssueBugList, this.redmineIssueLineBugList);
                fileName = "redmine_issuesReopened";
                break;
        }
        return this.redmineIssueLineBugList;
    }

    /**tests only
     *
     * @param id id of Issue that we want know its contained
     */
    public void watchRedmineIssuesTest(int id){
        RedmineUtils.setIssue(this.issueManager, id);
    }
}
