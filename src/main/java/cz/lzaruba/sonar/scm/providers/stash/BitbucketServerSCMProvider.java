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
package cz.lzaruba.sonar.scm.providers.stash;

import cz.lzaruba.sonar.scm.model.Analysis;
import cz.lzaruba.sonar.scm.model.Issue;
import cz.lzaruba.sonar.scm.providers.SCMProvider;
import cz.lzaruba.sonar.scm.utils.HttpUtils;
import cz.lzaruba.sonar.scm.utils.impl.HttpUtilsImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Implementation for the Bitbucket Server / Stash
 *
 * See the REST API documentation here {https://docs.atlassian.com/bitbucket-server/rest/7.11.1/bitbucket-code-insights-rest.html}
 * and here {https://docs.atlassian.com/bitbucket-server/rest/7.11.1/bitbucket-rest.html}
 *
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class BitbucketServerSCMProvider implements SCMProvider {

    private static final String DIFF_URL = "%s/rest/api/1.0/projects/%s/repos/%s/pull-requests/%s.diff";
    private static final String REPORT_URL = "%s/rest/insights/1.0/projects/%s/repos/%s/commits/%s/reports/sonar-report";
    private static final String ANNOTATIONS_URL = REPORT_URL + "/annotations";
    private static final String[] URL_PROPERTY_KEYS = {"host",
        "projectKey",
        "repositorySlug",
        "commitId"};

    private final HttpUtils httpUtils = new HttpUtilsImpl();

    @Override
    public String getId() {
        return "bitbucketServer";
    }

    @Override
    public String getDiff(Map<String, String> properties) {
        return httpUtils.httpGet(DIFF_URL,
            getHeaders(properties, null),
            properties,
            "host",
            "projectKey",
            "repositorySlug",
            "pullRequestId");
    }

    private Map<String, String> getHeaders(Map<String, String> properties, String contentType) {
        HashMap<String, String> headers = new HashMap<>(Collections.singletonMap(HttpUtils.HEADER_AUTHORIZATION,
            httpUtils.getBasicAuthHeader("username", "token", properties)));
        if (contentType != null) {
            headers.put(HttpUtils.HEADER_CONTENT_TYPE, contentType);
        }
        return headers;
    }

    @Override
    public void writeAnalysis(Analysis analysis) {
        deletePreviousReport(analysis);
        createReport(analysis);
    }

    private void createReport(Analysis analysis) {
        httpUtils.httpPut(REPORT_URL,
            httpUtils.getBody(getReportBody(analysis)),
            getHeaders(analysis.getProperties(), HttpUtils.CONTENT_TYPE_APPLICATION_JSON),
            analysis.getProperties(),
            URL_PROPERTY_KEYS);

        Annotations annotations = getAnnotationsBody(analysis);
        if (annotations.getAnnotations().isEmpty()) {
            return;
        }

        httpUtils.httpPost(ANNOTATIONS_URL,
            httpUtils.getBody(annotations),
            getHeaders(analysis.getProperties(), HttpUtils.CONTENT_TYPE_APPLICATION_JSON),
            analysis.getProperties(),
            URL_PROPERTY_KEYS);
    }

    private Annotations getAnnotationsBody(Analysis analysis) {
        return new Annotations().setAnnotations(analysis.getIssues().stream()
            .map(this::getAnnotation).collect(toList()));
    }

    private Annotation getAnnotation(Issue issue) {
        return new Annotation()
            .setType(mapType(issue.getType()))
            .setPath(issue.getComponent())
            .setSeverity(mapSeverity(issue.getSeverity()))
            .setLine(issue.getLine())
            .setMessage(issue.getMessage());
    }

    private Annotation.Severity mapSeverity(Issue.Severity severity) {
        if (severity == Issue.Severity.BLOCKER || severity == Issue.Severity.CRITICAL) {
            return Annotation.Severity.HIGH;
        }

        if (severity == Issue.Severity.MAJOR) {
            return Annotation.Severity.MEDIUM;
        }

        return Annotation.Severity.LOW;
    }

    private Annotation.Type mapType(Issue.Type type) {
        if (type == Issue.Type.VULNERABILITY || type == Issue.Type.SECURITY_HOTSPOT) {
            return Annotation.Type.VULNERABILITY;
        }

        if (type == Issue.Type.CODE_SMELL) {
            return Annotation.Type.CODE_SMELL;
        }

        return Annotation.Type.BUG;
    }



    private ReportRequest getReportBody(Analysis analysis) {
        return new ReportRequest()
            .setTitle("SonarQube Analysis")
            .setResult(analysis.getResult() == Analysis.Result.PASSED ? ReportRequest.Result.PASS : ReportRequest.Result.FAIL)
            .setReporter("Sonar SCM Plugin")
            .setData(analysis.getIssuesSummary().entrySet().stream()
                .sorted((e1, e2) -> -1 * Integer.compare(getSeverityIndex(e1.getKey()), getSeverityIndex(e2.getKey())))
                .map(this::mapIssueSummary)
                .collect(toList()));
    }

    private int getSeverityIndex(Issue.Severity severity) {
        return Arrays.binarySearch(Issue.Severity.values(), severity);
    }

    private ReportRequestData mapIssueSummary(Map.Entry<Issue.Severity, Long> record) {
        return new ReportRequestData(record.getKey().toString(), ReportRequestData.ReportRequestDataType.NUMBER, record.getValue());
    }

    private void deletePreviousReport(Analysis analysis) {
        httpUtils.httpDelete(REPORT_URL,
            getHeaders(analysis.getProperties(), null),
            analysis.getProperties(),
            URL_PROPERTY_KEYS);
    }

}