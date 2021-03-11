package cz.lzaruba.sonar.scm.providers;

import cz.lzaruba.sonar.scm.model.Analysis;

import java.util.Map;

public interface SCMProvider {

    String getId();
    String getDiff(Map<String, String> properties);
    void writeAnalysis(Analysis analysis);

}
