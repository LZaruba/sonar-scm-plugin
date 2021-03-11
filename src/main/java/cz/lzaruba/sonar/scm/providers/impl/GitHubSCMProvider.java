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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class GitHubSCMProvider implements SCMProvider {

    private static final String DIFF_URL = "%s/repos/%s/%s/pulls/%s.diff";
    public static final String ACCEPT_TYPE = "application/vnd.github.v3.diff";

    private final HttpUtils httpUtils = new HttpUtilsImpl();

    @Override
    public String getId() {
        return "github";
    }

    @Override
    public String getDiff(Map<String, String> properties) {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpUtils.HEADER_AUTHORIZATION, httpUtils.getBasicAuthHeader("username", "token", properties));
        headers.put(HttpUtils.HEADER_ACCEPT, ACCEPT_TYPE);

        return httpUtils.httpGet(DIFF_URL, headers, properties, "host", "owner", "repo", "pullNumber");
    }

    @Override
    public void writeAnalysis(Analysis analysis) {

    }
}
