package cz.lzaruba.sonar.scm.diff;

import cz.lzaruba.sonar.scm.diff.impl.DiffParserImpl;
import cz.lzaruba.sonar.scm.diff.model.Diff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Disabled
class UnifiedDiffReaderTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGithub() throws Exception {
        String file = new BufferedReader(
            new InputStreamReader(getClass().getResourceAsStream("/diff"), StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
        Diff parse = new DiffParserImpl().parseDiff(file);

        System.out.println(parse);
    }
}