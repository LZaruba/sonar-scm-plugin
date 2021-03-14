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
import cz.lzaruba.sonar.scm.diff.model.File;
import cz.lzaruba.sonar.scm.diff.model.Hunk;
import cz.lzaruba.sonar.scm.model.Issue;

import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class DiffIssueFilterImpl implements IssueFilter {

    private final Diff diff;

    public DiffIssueFilterImpl(Diff diff) {
        if (diff == null) {
            throw new IllegalArgumentException("Diff is required");
        }
        this.diff = diff;
    }

    @Override
    public boolean test(Issue issue) {
        return Optional.ofNullable(diff.getFiles()).orElse(emptyList())
            .stream()
            .anyMatch(f -> fileContainsIssue(f, issue));
    }

    private boolean fileContainsIssue(File file, Issue issue) {
        if (file.getToFile() == null || !file.getToFile().equals(issue.getComponent())) {
            return false;
        }

        if (issue.getLine() == null) { // issue is assigned to the whole file
            return true;
        }

        return Optional.ofNullable(file.getHunks()).orElse(emptyList())
            .stream()
            .anyMatch(h -> hunkContainsLine(h, issue.getLine()));
    }

    private boolean hunkContainsLine(Hunk hunk, Integer line) {
        int startLine = hunk.getFromLineStart();
        int endLine = startLine + hunk.getToNumLines();
        return line >= startLine && line <= endLine;
    }

}
