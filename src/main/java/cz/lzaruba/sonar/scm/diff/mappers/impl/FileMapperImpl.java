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

import com.github.stkent.githubdiffparser.models.Diff;
import cz.lzaruba.sonar.scm.diff.mappers.FileMapper;
import cz.lzaruba.sonar.scm.diff.mappers.HunkMapper;
import cz.lzaruba.sonar.scm.diff.model.File;

import java.util.stream.Collectors;

/**
 * @author Marcos Paulo Belasco de Almeida, marcos@marcosalmeida.dev, 2021
 */
public class FileMapperImpl implements FileMapper {

    private static final HunkMapper hunkMapper = new HunkMapperImpl();

    @Override
    public File diffToFile(Diff diff) {

        if (diff == null) {
            return null;
        }

        File file = new File();
        if (diff.getToFileName() != null) {
            file.setToFile(diff.getToFileName().replace("dst://", ""));
        }
        if (diff.getFromFileName() != null) {
            file.setFromFile(diff.getFromFileName().replace("src://", ""));
        }

        file.setHunks(diff.getHunks().stream().map(hunkMapper::libraryHunkToHunk).collect(Collectors.toList()));

        return file;
    }
}
