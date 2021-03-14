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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    public enum Type {
        CODE_SMELL, VULNERABILITY, BUG, SECURITY_HOTSPOT
    }

    public enum Severity {
        INFO, MINOR, MAJOR, CRITICAL, BLOCKER;

        public static List<Severity> getThresholdOrAbove(Severity threshold) {
            List<Severity> result = new ArrayList<>();
            boolean thresholdReached = false;
            for (Severity s : Severity.values()) {
                if (thresholdReached) {
                    result.add(s);
                    continue;
                }
                if (s == threshold) {
                    thresholdReached = true;
                    result.add(s);
                }
            }

            return result;
        }
    }

    private String key;
    private String ruleKey;
    private Severity severity;
    private Type type;
    private String message;

    private String component;
    private Integer line;

}
