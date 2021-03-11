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
package cz.lzaruba.sonar.scm;

import cz.lzaruba.sonar.scm.diff.DiffParser;
import cz.lzaruba.sonar.scm.diff.IssueFilter;
import cz.lzaruba.sonar.scm.diff.impl.DiffParserImpl;
import cz.lzaruba.sonar.scm.diff.impl.IssueFilterImpl;
import cz.lzaruba.sonar.scm.diff.model.Diff;
import cz.lzaruba.sonar.scm.model.Analysis;
import cz.lzaruba.sonar.scm.providers.SCMProvider;
import cz.lzaruba.sonar.scm.providers.SCMProviderFactory;
import cz.lzaruba.sonar.scm.providers.impl.SCMProviderFactoryImpl;
import cz.lzaruba.sonar.scm.utils.PropertyUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import static java.util.stream.Collectors.toList;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class AnalysisProcessor {

    private static final Logger LOG = Loggers.get(AnalysisProcessor.class);

    private final DiffParser diffParser = new DiffParserImpl();
    private final IssueFilter issueFilter = new IssueFilterImpl();
    private final PropertyUtils propertyUtils = new PropertyUtils();
    private final SCMProviderFactory providerFactory = new SCMProviderFactoryImpl();

    public void process(Analysis analysis) {
        SCMProvider scmProvider = providerFactory.getProvider(propertyUtils.p(analysis.getProperties(), "scm"));
        String diffInput = scmProvider.getDiff(analysis.getProperties());
        Diff diff = diffParser.parseDiff(diffInput);
        scmProvider.writeAnalysis(filterAnalysis(analysis, diff));
    }

    private Analysis filterAnalysis(Analysis analysis, Diff diff) {
        return new Analysis(
            analysis.getIssues().stream()
                .filter(i -> issueFilter.isPresent(i, diff))
                .collect(toList()),
            analysis.getProperties());
    }

}
