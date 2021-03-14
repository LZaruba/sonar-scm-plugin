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

import com.github.stkent.githubdiffparser.GitHubDiffParser;
import cz.lzaruba.sonar.scm.diff.mappers.FileMapper;
import cz.lzaruba.sonar.scm.diff.DiffParser;
import cz.lzaruba.sonar.scm.diff.mappers.impl.FileMapperImpl;
import cz.lzaruba.sonar.scm.diff.model.Diff;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class DiffParserImpl implements DiffParser {

    private final FileMapper fileMapper = new FileMapperImpl();
    private final GitHubDiffParser gitHubDiffParser = new GitHubDiffParser();

    @Override
    public Diff parseDiff(String input) {
        return new Diff(
            gitHubDiffParser
                .parse(input.getBytes(StandardCharsets.UTF_8))
                .stream()
                .map(fileMapper::diffToFile)
                .collect(Collectors.toList())
        );
    }

}
