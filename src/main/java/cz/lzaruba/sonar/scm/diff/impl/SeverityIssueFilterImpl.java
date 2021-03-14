package cz.lzaruba.sonar.scm.diff.impl;

import cz.lzaruba.sonar.scm.diff.IssueFilter;
import cz.lzaruba.sonar.scm.model.Issue;

import java.util.List;

public class SeverityIssueFilterImpl implements IssueFilter {

    private final List<Issue.Severity> validSeverities;

    public SeverityIssueFilterImpl(Issue.Severity minSeverity) {
        validSeverities = Issue.Severity.getThresholdOrAbove(minSeverity);
    }

    @Override
    public boolean test(Issue issue) {
        return validSeverities.contains(issue.getSeverity());
    }
}
