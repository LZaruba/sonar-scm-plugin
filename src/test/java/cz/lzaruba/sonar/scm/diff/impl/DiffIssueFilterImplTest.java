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

import java.util.Arrays;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
class DiffIssueFilterImplTest {

    @Test
    void nullInput() {
        assertThrows(IllegalArgumentException.class, () -> new DiffIssueFilterImpl(null));
    }

    @Test
    void emptyDiff() {
        DiffIssueFilterImpl filter = new DiffIssueFilterImpl(new Diff().setFiles(emptyList()));

        assertThat(filter.test(new Issue().setComponent("component"))).isFalse();
    }

    @Test
    void nullFiles() {
        DiffIssueFilterImpl filter = new DiffIssueFilterImpl(new Diff());

        assertThat(filter.test(new Issue().setComponent("component"))).isFalse();
    }

    @Test
    void nullToFileInDiff() {
        DiffIssueFilterImpl filter = new DiffIssueFilterImpl(new Diff().setFiles(Arrays.asList(
            new File()
        )));

        assertThat(filter.test(new Issue().setComponent("component"))).isFalse();
    }

    @Test
    void matchFile() {
        DiffIssueFilterImpl filter = new DiffIssueFilterImpl(new Diff().setFiles(Arrays.asList(
            new File().setToFile("component")
        )));

        assertThat(filter.test(new Issue().setComponent("component"))).isTrue();
    }

    @Test
    void hunkNoMatch() {
        DiffIssueFilterImpl filter = new DiffIssueFilterImpl(new Diff().setFiles(Arrays.asList(
            new File().setToFile("component")
        )));

        assertThat(filter.test(new Issue().setComponent("component").setLine(11))).isFalse();
    }

    @Test
    void hunkMatch() {
        DiffIssueFilterImpl filter = new DiffIssueFilterImpl(new Diff().setFiles(Arrays.asList(
            new File().setToFile("component").setHunks(Arrays.asList(
                new Hunk().setFromLineStart(5).setToNumLines(10) // lines 5-15
            ))
        )));

        assertThat(filter.test(new Issue().setComponent("component").setLine(11))).isTrue();
    }

}
