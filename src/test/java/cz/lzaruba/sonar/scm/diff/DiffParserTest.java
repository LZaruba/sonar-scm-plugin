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

import com.github.difflib.unifieddiff.UnifiedDiff;
import com.github.difflib.unifieddiff.UnifiedDiffReader;
import cz.lzaruba.sonar.scm.diff.impl.DiffParserImpl;
import cz.lzaruba.sonar.scm.diff.model.Diff;
import cz.lzaruba.sonar.scm.diff.model.File;
import cz.lzaruba.sonar.scm.diff.model.Hunk;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lukas Zaruba, lukas.zaruba@lundegaard.eu, 2021
 */
class DiffParserTest {

    @Test
    void simple() throws IOException, URISyntaxException {
        Diff result = new DiffParserImpl().parseDiff(getResource("new"));

        assertThat(result).isEqualTo(new Diff().setFiles(Arrays.asList(
            new File()
                .setFromFile("src://user-account-service-application/src/integration-test/groovy/com/klarna/useraccount/service/flow/RegressionTest.groovy")
                .setToFile("dst://user-account-service-application/src/integration-test/groovy/com/klarna/useraccount/service/flow/RegressionTest.groovy")
                .setHunks(Arrays.asList(
                    new Hunk()
                        .setFromLineStart(200)
                        .setFromNumLines(21)
                        .setToLineStart(200)
                        .setToNumLines(21))))));
    }

    @Test
    void simple2() throws IOException, URISyntaxException {
        UnifiedDiff simple = UnifiedDiffReader.parseUnifiedDiff(new ByteArrayInputStream(getResource("new").getBytes(StandardCharsets.UTF_8)));
    }

    private String getResource(String name) throws URISyntaxException, IOException {
        URL url = getClass().getClassLoader().getResource("diff/" + name + ".diff");
        return new String(Files.readAllBytes(Paths.get(url.toURI())), StandardCharsets.UTF_8);
    }

}