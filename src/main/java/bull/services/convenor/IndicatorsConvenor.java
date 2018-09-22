package bull.services.convenor;

import bull.internal.issues.RedmineIssueBug;
import bull.internal.lines.RedmineIssueLineBug;
import bull.services.extractor.internal.enums.MethodChoiceEnum;

import java.util.List;

public interface IndicatorsConvenor {
    List<RedmineIssueLineBug> sortToGetlineToWrite(List<RedmineIssueBug> redmineIssueBugList, MethodChoiceEnum methodChoiceEnum);
}
