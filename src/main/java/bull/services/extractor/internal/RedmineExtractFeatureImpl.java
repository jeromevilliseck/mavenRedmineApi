package bull.services.extractor.internal;

import bull.connector.RedmineConnection;
import bull.internal.utils.RedmineUtils;
import bull.internal.issues.RedmineIssueFeature;
import bull.logger.Logger;
import bull.services.extractor.IndicatorsExtractor;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;

import java.util.ArrayList;
import java.util.List;

public class RedmineExtractFeatureImpl implements IndicatorsExtractor<RedmineIssueFeature> {
    /*Members*/
    private IssueManager issueManager;
    private List<RedmineIssueFeature> redmineIssueFeatureList;

    /*Constructor*/
    public RedmineExtractFeatureImpl(){
        this.redmineIssueFeatureList = new ArrayList<>();
    }

    public List<RedmineIssueFeature> fetch(){
        this.makeConnection();
        return this.treatment("sics", "laffic", "pte-contact");
    }

    /*Methods*/
    private void makeConnection(){
        this.issueManager = new RedmineConnection(RedmineConnection.Project.SICS).getIssueManager();
    }

    private List<RedmineIssueFeature> treatment(String... projectNames){
        for(final String project : projectNames){
            int localSize = RedmineUtils.getSizeOfIssuesListWithJournalLines(issueManager, project, 4);

            try {
                for (int j = 0; j < localSize; j += 100) {
                    RedmineUtils.setMultipleFeatureIssuesWithTheirStatus(issueManager, j, localSize, redmineIssueFeatureList, project);
                }
            } catch (RedmineException e) {
                Logger.getInstance().fatal("Fatal Error in Recovery of Issues", e);
            }
        }
        return this.redmineIssueFeatureList;
    }

    /**tests only
     *
     * @param id id of Issue that we want know its contained
     */
    public void watchRedmineIssuesTest(int id){
        RedmineUtils.setIssue(this.issueManager, id);
    }
}
