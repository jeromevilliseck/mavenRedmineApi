package bull.services.extractor.internal;

import bull.exception.CriticityLevelEnum;
import bull.exception.MyRuntimeException;
import bull.connector.RedmineConnection;
import bull.internal.utils.RedmineUtils;
import bull.internal.issues.RedmineIssueBug;
import bull.services.extractor.IndicatorsExtractor;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;

import java.util.ArrayList;
import java.util.List;

public class RedmineExtractBugImpl implements IndicatorsExtractor<RedmineIssueBug> {
    /*Members*/
    private IssueManager issueManager;
    private List<RedmineIssueBug> redmineIssueBugList;

    /*Constructor*/
    public RedmineExtractBugImpl(){
        this.redmineIssueBugList = new ArrayList<>();
    }

    /*Fetch*/
    public List<RedmineIssueBug> fetch(){
        this.makeConnection();
        return this.treatment("sics", "laffic", "pte-contact");
    }

    /*Methods*/
    private void makeConnection(){
        this.issueManager = new RedmineConnection(RedmineConnection.Project.SICS).getIssueManager();
    }

    private List<RedmineIssueBug> treatment(String... projectNames){
        for(final String project : projectNames){
            RedmineUtils.displaySizeOfIssuesList(issueManager, project);
            int localSize = RedmineUtils.getSizeOfIssuesListWithJournalLines(issueManager, project, 1);

            try {
                for (int j = 0; j < localSize; j += 100) {
                    RedmineUtils.setMultipleBugIssuesWithTheirStatus(issueManager, j, localSize, redmineIssueBugList, project);
                }
            } catch (RedmineException e) {
                throw new MyRuntimeException(CriticityLevelEnum.FATAL, "Fatal Error in Recovery of Issues", e);
            }
        }
        return this.redmineIssueBugList;
    }

}
