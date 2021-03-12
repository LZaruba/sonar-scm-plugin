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
package cz.lzaruba.sonar.scm.utils.impl;

import com.google.gson.Gson;
import cz.lzaruba.sonar.scm.utils.HttpUtils;
import cz.lzaruba.sonar.scm.utils.PropertyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class HttpUtilsImpl implements HttpUtils {

    private static final int TIMEOUT = 5000;

    private final PropertyUtils propertyUtils = new PropertyUtils();

    @Override
    public String httpGet(String urlPattern, Map<String, String> headers, Map<String, String> properties, String ... urlVariableKeys) {
        return http("GET", null, urlPattern, headers, properties, urlVariableKeys);
    }

    private String http(String method, String body, String urlPattern, Map<String, String> headers, Map<String, String> properties, String[] urlVariableKeys) {
        String[] urlVariables = Stream.of(urlVariableKeys)
                .map(key -> propertyUtils.p(properties, key))
                .toArray(String[]::new);
        String path = String.format(urlPattern, urlVariables);

        HttpURLConnection con = null;
        try {
            URL url = new URL(path);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            for (Map.Entry<String, String> e : headers.entrySet()) {
                con.setRequestProperty(e.getKey(), e.getValue());
            }

            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);
            if (body != null) {
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8);
                writer.write(body);
                writer.flush();
            }
            con.getResponseCode();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {

                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine).append("\n");
                }
                return content.toString().trim();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while loading data from " + path, e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    @Override
    public String httpPost(String urlPattern, String body, Map<String, String> headers, Map<String, String> properties, String... urlVariableKeys) {
        return http("POST", body, urlPattern, headers, properties, urlVariableKeys);
    }

    @Override
    public String httpPut(String urlPattern, String body, Map<String, String> headers, Map<String, String> properties, String... urlVariableKeys) {
        return http("PUT", body, urlPattern, headers, properties, urlVariableKeys);
    }

    @Override
    public String httpDelete(String urlPattern, Map<String, String> headers, Map<String, String> properties, String... urlVariableKeys) {
        return http("DELETE", null, urlPattern, headers, properties, urlVariableKeys);
    }

    @Override
    public String getBasicAuthHeader(String usernameKey, String passwordKey, Map<String, String> properties) {
        String auth = propertyUtils.p(properties, usernameKey) + ":" + propertyUtils.p(properties, passwordKey);
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }

    @Override
    public String getBody(Object body) {
        return new Gson().toJson(body);
    }

}
