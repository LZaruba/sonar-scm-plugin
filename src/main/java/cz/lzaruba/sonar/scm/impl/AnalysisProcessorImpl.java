/*
 * Copyright 2021 Lukas Zaruba, lukas.zaruba@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.lzaruba.sonar.scm.impl;

import cz.lzaruba.sonar.scm.AnalysisProcessor;
import cz.lzaruba.sonar.scm.diff.DiffParser;
import cz.lzaruba.sonar.scm.diff.impl.DiffParserImpl;
import cz.lzaruba.sonar.scm.diff.impl.DiffIssueFilterImpl;
import cz.lzaruba.sonar.scm.diff.impl.SeverityIssueFilterImpl;
import cz.lzaruba.sonar.scm.diff.model.Diff;
import cz.lzaruba.sonar.scm.model.Analysis;
import cz.lzaruba.sonar.scm.model.Issue;
import cz.lzaruba.sonar.scm.providers.SCMProvider;
import cz.lzaruba.sonar.scm.providers.SCMProviderFactory;
import cz.lzaruba.sonar.scm.providers.impl.SCMProviderFactoryImpl;
import cz.lzaruba.sonar.scm.utils.PropertyUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class AnalysisProcessorImpl implements AnalysisProcessor {

    private static final Logger LOG = Loggers.get(AnalysisProcessorImpl.class);
    private static final String MIN_SEVERITY_KEY = "sonar.analysis.minSeverity";

    private final DiffParser diffParser = new DiffParserImpl();
    private final PropertyUtils propertyUtils = new PropertyUtils();
    private final SCMProviderFactory providerFactory = new SCMProviderFactoryImpl();

    @Override
    public void process(String sonarProjectKey, List<Issue> issues, Map<String, String> analysisProperties) {
        SCMProvider scmProvider = providerFactory.getProvider(propertyUtils.p(analysisProperties, "scm"));
        String diffInput = scmProvider.getDiff(analysisProperties);
        Diff diff = diffParser.parseDiff(diffInput);
        issues.forEach(i -> updateIssuePath(i, sonarProjectKey, analysisProperties));
        scmProvider.writeAnalysis(createAnalysis(sonarProjectKey, filterIssues(issues, diff, getMinSeverity(analysisProperties)), analysisProperties));
    }

    private void updateIssuePath(Issue i, String sonarProjectKey, Map<String, String> analysisProperties) {
        String projectKeyPrefix = sonarProjectKey + ":";
        String path = i.getComponent();
        if (path.startsWith(projectKeyPrefix)) {
            path = path.substring(projectKeyPrefix.length());
        }
        Optional<String> prefix = propertyUtils.pOptional(analysisProperties, "filePathPrefix");
        if (prefix.isPresent()) {
            path = prefix.get() + path;
        }
        i.setComponent(path);
    }

    private Issue.Severity getMinSeverity(Map<String, String> analysisProperties) {
        return Issue.Severity.valueOf(propertyUtils.p(analysisProperties, MIN_SEVERITY_KEY, Issue.Severity.MAJOR.name()));
    }

    private Analysis createAnalysis(String sonarProjectKey, List<Issue> issues, Map<String, String> analysisProperties) {
        return new Analysis(sonarProjectKey, issues, analysisProperties);
    }

    private List<Issue> filterIssues(List<Issue> issues, Diff diff, Issue.Severity minSeverity) {
        return issues.stream()
                .filter(new SeverityIssueFilterImpl(minSeverity))
                .filter(new DiffIssueFilterImpl(diff))
                .collect(toList());
    }

}
