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
package cz.lzaruba.sonar.scm.providers;

import cz.lzaruba.sonar.scm.model.Analysis;
import cz.lzaruba.sonar.scm.utils.PropertyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class GitHubSCMProvider implements SCMProvider {

    private static final String DIFF_URL = "%s/repos/%s/%s/pulls/%s.diff";

    private final PropertyUtils propertyUtils = new PropertyUtils();

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getDiff(Map<String, String> properties) {
        String path = String.format(DIFF_URL,
                propertyUtils.p(properties, "host"),
                propertyUtils.p(properties, "owner"),
                propertyUtils.p(properties, "repo"),
                propertyUtils.p(properties, "pullNumber"));

        String auth = propertyUtils.p(properties, "username") + ":" + propertyUtils.p(properties, "token");
        HttpURLConnection con = null;
        try {
            URL url = new URL(path);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()));
            con.setRequestProperty("Accept", "application/vnd.github.v3.diff");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
//                if ("\\ No newline at end of file".equals(inputLine)) {
//                    continue;
//                }
                content.append(inputLine).append("\n");
            }
            in.close();
            return content.toString().trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
//        WebTarget target = client.target(url);
//        return ClientBuilder.newClient()
//                .register(HttpAuthenticationFeature.basic(propertyUtils.p(properties, "username"), propertyUtils.p(properties, "token")))
//                .target(url)
//                .request("application/vnd.github.VERSION.diff")
//                .get(String.class);

    }

    @Override
    public void writeAnalysis(Analysis analysis) {

    }
}
