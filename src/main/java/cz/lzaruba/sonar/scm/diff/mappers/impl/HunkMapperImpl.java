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
package cz.lzaruba.sonar.scm.diff.mappers.impl;

import cz.lzaruba.sonar.scm.diff.mappers.HunkMapper;
import cz.lzaruba.sonar.scm.diff.model.Hunk;

/**
 * @author Marcos Paulo Belasco de Almeida, marcos@marcosalmeida.dev, 2021
 */
public class HunkMapperImpl implements HunkMapper {
    @Override
    public Hunk libraryHunkToHunk(com.github.stkent.githubdiffparser.models.Hunk hunk) {

        if (hunk == null) {
            return null;
        }

        Hunk responseHunk = new Hunk();
        if (hunk.getFromFileRange() != null) {
            responseHunk.setFromLineStart(hunk.getFromFileRange().getLineStart());
            responseHunk.setFromNumLines(hunk.getFromFileRange().getLineCount());
        }

        if (hunk.getToFileRange() != null) {
            responseHunk.setToLineStart(hunk.getToFileRange().getLineStart());
            responseHunk.setToNumLines(hunk.getToFileRange().getLineCount());
        }

        return responseHunk;
    }
}
