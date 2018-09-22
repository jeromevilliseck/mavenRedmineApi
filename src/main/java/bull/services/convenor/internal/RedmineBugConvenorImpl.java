package bull.services.convenor.internal;

import bull.internal.utils.RedmineUtils;
import bull.internal.issues.RedmineIssueBug;
import bull.internal.lines.RedmineIssueLineBug;
import bull.services.convenor.IndicatorsConvenor;
import bull.services.extractor.internal.enums.MethodChoiceEnum;

import java.util.ArrayList;
import java.util.List;

public class RedmineBugConvenorImpl implements IndicatorsConvenor {
    /*Members*/
    private List<RedmineIssueLineBug> redmineIssueLineBugList;

    /*Constructor*/
    public RedmineBugConvenorImpl(){
        this.redmineIssueLineBugList = new ArrayList<>();
    }

    /*Method*/
    public List<RedmineIssueLineBug> sortToGetlineToWrite(List<RedmineIssueBug> redmineIssueBugList, MethodChoiceEnum methodChoiceEnum){
        return this.sortRedmineIssues(redmineIssueBugList, methodChoiceEnum);
    }

    private List<RedmineIssueLineBug> sortRedmineIssues(List<RedmineIssueBug> redmineIssueBugList, MethodChoiceEnum methodChoiceEnum){
        switch (methodChoiceEnum){
            case ALLSTATUS:
                this.redmineIssueLineBugList.clear();
                RedmineUtils.placeRedmineIssuesBugWithAllStatusInLineList(redmineIssueBugList, this.redmineIssueLineBugList);
                break;
            case LASTSTATUS:
                this.redmineIssueLineBugList.clear();
                RedmineUtils.placeRedmineIssuesWithOnlyLastStatusByDateInLineList(redmineIssueBugList, this.redmineIssueLineBugList);
                break;
            case REOPENED:
                this.redmineIssueLineBugList.clear();
                RedmineUtils.placeRedmineIssuesWithOnlyReopenedStatusInLineList(redmineIssueBugList, this.redmineIssueLineBugList);
                break;
        }
        return this.redmineIssueLineBugList;
    }
}
