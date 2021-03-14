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
package cz.lzaruba.sonar.scm.model;

import cz.lzaruba.sonar.scm.utils.PropertyUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Representation of the finished analysis, incl. issues and properties
 *
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
@Getter
@RequiredArgsConstructor
@ToString
public class Analysis {

    private static final String FAIL_THRESHOLD_KEY = "sonar.analysis.failThreshold";

    public enum Result {
        PASSED, FAILED
    }

    private final PropertyUtils propertyUtils = new PropertyUtils();

    private final String sonarProjectKey;
    private final List<Issue> issues;
    private final Map<String, String> properties;
    private final Map<Issue.Severity, Long> issuesSummary;
    private final Result result;

    public Analysis(String sonarProjectKey, List<Issue> issues, Map<String, String> properties) {
        this.sonarProjectKey = sonarProjectKey;
        this.issues = issues;
        this.properties = properties;
        this.issuesSummary = issues.stream()
            .collect(groupingBy(Issue::getSeverity, counting()));
        this.result = evaluateResult();
    }

    private Result evaluateResult() {
        List<Issue.Severity> threshold = Issue.Severity.getThresholdOrAbove(
            Issue.Severity.valueOf(propertyUtils.p(properties, FAIL_THRESHOLD_KEY, Issue.Severity.MAJOR.name())));
        boolean failed = issues.stream()
            .anyMatch(i -> threshold.contains(i.getSeverity()));
        return failed ? Result.FAILED : Result.PASSED;
    }

}
