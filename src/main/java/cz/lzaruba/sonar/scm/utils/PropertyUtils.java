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
import java.util.Optional;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class PropertyUtils {

    private static final String PROPERTY_PREFIX = "sonar.analysis.";

    public String p(Map<String, String> properties, String key) {
        String value = properties.get(PROPERTY_PREFIX + key);
        if (value == null) {
            throw new IllegalStateException(String.format("Missing property '%s'", PROPERTY_PREFIX + key));
        }
        return value;
    }

    public Optional<String> pOptional(Map<String, String> properties, String key) {
        return Optional.ofNullable(properties.get(PROPERTY_PREFIX + key));
    }

    public String p(Map<String, String> properties, String key, String defaultValue) {
        String value = properties.get(PROPERTY_PREFIX + key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

}
