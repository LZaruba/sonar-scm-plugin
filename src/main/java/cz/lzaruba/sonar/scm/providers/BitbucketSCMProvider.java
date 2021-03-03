package cz.lzaruba.sonar.scm.providers;

import cz.lzaruba.sonar.scm.model.Analysis;
import cz.lzaruba.sonar.scm.utils.PropertyUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.Map;

public class BitbucketSCMProvider implements SCMProvider {

    private static final String DIFF_URL = "%s/rest/api/1.0/projects/%s/repos/%s/pull-requests/%s.diff";

    private final Client client = ClientBuilder.newClient();
    private final PropertyUtils propertyUtils = new PropertyUtils();

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getDiff(Map<String, String> properties) {
        String url = String.format(DIFF_URL,
            propertyUtils.p(properties, "host"),
            propertyUtils.p(properties, "projectKey"),
            propertyUtils.p(properties, "repositorySlug"),
            propertyUtils.p(properties, "pullRequestId"));
        //client.target(url)
        return null;
    }

    @Override
    public void writeAnalysis(Analysis analysis) {

    }

}
