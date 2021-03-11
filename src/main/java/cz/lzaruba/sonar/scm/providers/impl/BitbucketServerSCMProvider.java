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
package cz.lzaruba.sonar.scm.providers.impl;

import cz.lzaruba.sonar.scm.model.Analysis;
import cz.lzaruba.sonar.scm.providers.SCMProvider;
import cz.lzaruba.sonar.scm.utils.HttpUtils;
import cz.lzaruba.sonar.scm.utils.impl.HttpUtilsImpl;

import java.util.Collections;
import java.util.Map;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class BitbucketServerSCMProvider implements SCMProvider {

    private static final String DIFF_URL = "%s/rest/api/1.0/projects/%s/repos/%s/pull-requests/%s.diff";
    private static final String REPORT_URL = "%s/rest/insights/1.0/projects/%s/repos/%s/commits/%s/reports/%s";

    private final HttpUtils httpUtils = new HttpUtilsImpl();

    @Override
    public String getId() {
        return "bitbucketServer";
    }

    @Override
    public String getDiff(Map<String, String> properties) {
        return httpUtils.httpGet(DIFF_URL,
                Collections.singletonMap(HttpUtils.HEADER_AUTHORIZATION,
                    httpUtils.getBasicAuthHeader("username", "token", properties)),
                properties,
                "host",
                "projectKey",
                "repositorySlug",
                "pullRequestId");
    }

    @Override
    public void writeAnalysis(Analysis analysis) {

    }

}

/*
Create a report
Content-Type: application/json

{
    "title": "Lukas Test Report",
    "details": "This pull request introduces XX new dependency vulnerabilities.",
    "reporter": "mySystem",
    "link": "http://www.mysystem.com/reports/001",
    "result": "FAIL",
    "data": [
        {
            "title": "Test Code Coverage",
            "type": "PERCENTAGE",
            "value": 60.3
        },
        {
            "title": "Safe to merge?",
            "type": "BOOLEAN",
            "value": false
        }
    ]
}
 */

/*
Add annotations
Content-Type: application/json

{
  "annotations": [
    {
      "message": "global annotation",
      "severity": "LOW",
      "link": "http://someurl"
    },
    {
      "path": ".../.../..../.../xxx.java",
      "message": "file annotation",
      "severity": "MEDIUM",
      "link": "http://someurl",
      "type": "VULNERABILITY"
    },
    {
      "path": ".../.../..../.../xxx.java",
      "line": 8,
      "message": "line annotation",
      "severity": "HIGH",
      "link": "http://someurl",
      "type": "CODE_SMELL"
    },
    {
      "path": ".../.../..../.../xxx.java",
      "line": 11,
      "message": "line annotation",
      "severity": "HIGH",
      "link": "http://someurl",
      "type": "BUG"
    },
    {
      "path": ".../.../..../.../xxx.java",
      "line": 23,
      "message": "line annotation",
      "severity": "HIGH",
      "link": "http://someurl",
      "type": "BUG"
    }
  ]
}
 */