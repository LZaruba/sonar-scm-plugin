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
package cz.lzaruba.sonar.scm.providers;

import cz.lzaruba.sonar.scm.providers.impl.BitbucketServerSCMProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

/**
 * @author Lukas Zaruba, lukas.zaruba@lundegaard.eu, 2021
 */
class BitbucketServerSCMProviderTest {

    @Test
    void loadDiff() throws URISyntaxException, IOException {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("sonar.analysis.host", "xxxx");
        properties.put("sonar.analysis.projectKey", "xxxx");
        properties.put("sonar.analysis.repositorySlug", "xxx");
        properties.put("sonar.analysis.pullRequestId", "xxx");
        properties.put("sonar.analysis.username", "xxxx");
        properties.put("sonar.analysis.token", "xxx");

        String diff = new BitbucketServerSCMProvider().getDiff(properties);
        Path path = Paths.get("new.diff");
        Files.write(path, diff.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    private String getResource(String name) throws URISyntaxException, IOException {
        URL url = getClass().getClassLoader().getResource("diff/" + name + ".diff");
        return new String(Files.readAllBytes(Paths.get(url.toURI())), StandardCharsets.UTF_8);
    }

}