package bull;

import java.util.List;

import bull.exception.MyRuntimeException;
import bull.internal.issues.Upsource;
import bull.logger.Logger;

import bull.services.extractor.internal.UpsourceExtractImpl;
import bull.services.writer.IndicatorsWriter;
import bull.services.writer.internal.CSVUpsourceWriterImpl;

public class App
{

    public static void main(String[] args){
        try{
            Logger.getInstance().info("Data extraction tool : Redmine, Upsource, Sonar. \n Please wait...");
            /*Service Data Extraction*/
//            List<RedmineIssueBug> redmineIssueBugsList = new RedmineExtractBugImpl().fetch(); //Redmine
            List<Upsource> upsourceRevisionsList = new UpsourceExtractImpl().fetch(); //Upsource

            /*Service Convenor*/
//            IndicatorsConvenor convenor = new RedmineBugConvenorImpl();

            /*Service Writer*/
//            IndicatorsWriter redmineWriter = new CSVRedmineWriterImpl();
            IndicatorsWriter<Upsource> upsourceWriter = new CSVUpsourceWriterImpl();

            /*Write CSV Files*/
//            redmineWriter.writeIssues(convenor.sortToGetlineToWrite(redmineIssueBugsList, MethodChoiceEnum.ALLSTATUS), "redmineIssuesList_ALL_STATUS");
//            redmineWriter.writeIssues(convenor.sortToGetlineToWrite(redmineIssueBugsList, MethodChoiceEnum.LASTSTATUS), "redmineIssuesList_LAST_STATUS");
//            redmineWriter.writeIssues(convenor.sortToGetlineToWrite(redmineIssueBugsList, MethodChoiceEnum.REOPENED), "redmineIssuesList_REOPENED");
            upsourceWriter.writeIssues(upsourceRevisionsList, "upsource_file_test");

        }catch(MyRuntimeException e){
            switch (e.getCriticity()){
                case INFO:
                    Logger.getInstance().info(e);
                    break;
                case WARNING:
                    Logger.getInstance().warn(e);
                    break;
                case FATAL:
                    Logger.getInstance().fatal(e);
                    break;
            }
        }
    }
}
