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
package cz.lzaruba.sonar.scm.utils;

import java.util.Map;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public interface HttpUtils {

    String HEADER_ACCEPT = "Accept";
    String HEADER_AUTHORIZATION = "Authorization";
    String HEADER_CONTENT_TYPE = "Content-Type";

    String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    String httpGet(String urlPattern, Map<String, String> headers, Map<String, String> properties, String ... urlVariableKeys);
    String httpPost(String urlPattern, String body, Map<String, String> headers, Map<String, String> properties, String ... urlVariableKeys);
    String httpPut(String urlPattern, String body, Map<String, String> headers, Map<String, String> properties, String ... urlVariableKeys);
    String httpDelete(String urlPattern, Map<String, String> headers, Map<String, String> properties, String ... urlVariableKeys);

    String getBasicAuthHeader(String usernameKey, String passwordKey, Map<String, String> properties);
    String getBody(Object body);
}
