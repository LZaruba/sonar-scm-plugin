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

import cz.lzaruba.sonar.scm.diff.model.Diff;
import cz.lzaruba.sonar.scm.diff.model.File;
import cz.lzaruba.sonar.scm.diff.model.Hunk;
import cz.lzaruba.sonar.scm.model.Issue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
class IssueFilterImplTest {

    @Test
    void nullIssue() {
        assertThrows(NullPointerException.class,
                () -> new IssueFilterImpl().isPresent(null, new Diff()));
    }

    @Test
    void nullDiff() {
        assertThrows(NullPointerException.class,
                () -> new IssueFilterImpl().isPresent(getIssue(), null));
    }

    @Test
    void emptyDiff() {
        assertThat(new IssueFilterImpl().isPresent(getIssue(), new Diff()))
                .isFalse();
    }

    @Test
    void noMatch() {
        assertThat(new IssueFilterImpl().isPresent(getIssue(),
                new Diff()
                    .setFiles(List.of(
                            new File().setToFile("somefile")
                                    .setHunks(List.of(
                                            new Hunk().setToLineStart(60).setToNumLines(7)))))))
                .isFalse();
    }

    @Test
    void differentPosition() {
        assertThat(new IssueFilterImpl().isPresent(getIssue(),
                new Diff()
                        .setFiles(List.of(
                                new File().setToFile("src/main/java/package/File.java")
                                        .setHunks(List.of(
                                                new Hunk().setToLineStart(60).setToNumLines(7)))))))
                .isFalse();
    }

    @Test
    void nullHunks() {
        assertThat(new IssueFilterImpl().isPresent(getIssue(),
                new Diff()
                        .setFiles(List.of(
                                new File().setToFile("src/main/java/package/File.java")))))
                .isFalse();
    }

    @Test
    void match() {
        assertThat(new IssueFilterImpl().isPresent(getIssue(),
                new Diff()
                        .setFiles(List.of(
                                new File().setToFile("src/main/java/package/File.java")
                                        .setHunks(List.of(
                                                new Hunk().setToLineStart(70).setToNumLines(9)))))))
                .isTrue();
    }

    private Issue getIssue() {
        return new Issue("key", "ruleKey", "MINOR", "bug",
                "test", "test:src/main/java/package/File.java", 76);
    }

}
