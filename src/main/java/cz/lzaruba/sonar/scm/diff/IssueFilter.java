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
package cz.lzaruba.sonar.scm.diff;

import cz.lzaruba.sonar.scm.diff.model.Diff;
import cz.lzaruba.sonar.scm.model.Issue;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public interface IssueFilter {

    /**
     * Returns true if the given issue location is matching at least one of the diff hunks
     *
     * Prefix of 'test:' is striped from the issue's component path
     */
    boolean isPresent(Issue issue, Diff diff);

}
