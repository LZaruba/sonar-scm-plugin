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
package cz.lzaruba.sonar.scm.diff.impl;

import cz.lzaruba.sonar.scm.diff.IssueFilter;
import cz.lzaruba.sonar.scm.diff.model.Diff;
import cz.lzaruba.sonar.scm.model.Issue;

import java.util.Objects;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class IssueFilterImpl implements IssueFilter {

    private static final String TEST_PREFIX = "test:";

    @Override
    public boolean isPresent(Issue issue, Diff diff) {
        Objects.requireNonNull(issue, "Issue is required");
        Objects.requireNonNull(diff, "Diff is required");

        if (diff.getFiles() == null) {
            return false;
        }
        return diff.getFiles().stream()
                .filter(f -> f.getHunks() != null)
                .filter(f -> isComponentMatch(issue.getComponent(), f.getToFile()))
                .flatMap(f -> f.getHunks().stream())
                .anyMatch(h -> h.getToLineStart() <= issue.getLine()
                    && (h.getToLineStart() + h.getToNumLines()) >= issue.getLine());
    }

    private boolean isComponentMatch(String component, String toFile) {
        if (!component.startsWith(TEST_PREFIX)) {
            return false;
        }
        return component.substring(TEST_PREFIX.length()).equals(toFile);
    }

}
