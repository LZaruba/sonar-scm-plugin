package cz.lzaruba.sonar.scm.providers;

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