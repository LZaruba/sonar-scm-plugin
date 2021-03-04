package cz.lzaruba.sonar.scm.providers;

import cz.lzaruba.sonar.scm.model.Analysis;
import cz.lzaruba.sonar.scm.utils.PropertyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

public class BitbucketServerSCMProvider implements SCMProvider {

    private static final String DIFF_URL = "%s/rest/api/1.0/projects/%s/repos/%s/pull-requests/%s.diff";
    private static final String REPORT_URL = "%s/rest/insights/1.0/projects/%s/repos/%s/commits/%s/reports/%s";

    private final PropertyUtils propertyUtils = new PropertyUtils();

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getDiff(Map<String, String> properties) {
        String path = String.format(DIFF_URL,
            propertyUtils.p(properties, "host"),
            propertyUtils.p(properties, "projectKey"),
            propertyUtils.p(properties, "repositorySlug"),
            propertyUtils.p(properties, "pullRequestId"));

        String auth = propertyUtils.p(properties, "username") + ":" + propertyUtils.p(properties, "token");
        HttpURLConnection con = null;
        try {
            URL url = new URL(path);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()));
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
//                if ("\\ No newline at end of file".equals(inputLine)) {
//                    continue;
//                }
                content.append(inputLine).append("\n");
            }
            in.close();
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    @Override
    public void writeAnalysis(Analysis analysis) {

    }

}

/*
Create a report
Content-Type: application/json

{
    "title": "Lukas Test Report",
    "details": "This pull request introduces XX new dependency vulnerabilities.",
    "reporter": "mySystem",
    "link": "http://www.mysystem.com/reports/001",
    "result": "FAIL",
    "data": [
        {
            "title": "Test Code Coverage",
            "type": "PERCENTAGE",
            "value": 60.3
        },
        {
            "title": "Safe to merge?",
            "type": "BOOLEAN",
            "value": false
        }
    ]
}
 */

/*
Add annotations
Content-Type: application/json

{
  "annotations": [
    {
      "message": "global annotation",
      "severity": "LOW",
      "link": "http://someurl"
    },
    {
      "path": ".../.../..../.../xxx.java",
      "message": "file annotation",
      "severity": "MEDIUM",
      "link": "http://someurl",
      "type": "VULNERABILITY"
    },
    {
      "path": ".../.../..../.../xxx.java",
      "line": 8,
      "message": "line annotation",
      "severity": "HIGH",
      "link": "http://someurl",
      "type": "CODE_SMELL"
    },
    {
      "path": ".../.../..../.../xxx.java",
      "line": 11,
      "message": "line annotation",
      "severity": "HIGH",
      "link": "http://someurl",
      "type": "BUG"
    },
    {
      "path": ".../.../..../.../xxx.java",
      "line": 23,
      "message": "line annotation",
      "severity": "HIGH",
      "link": "http://someurl",
      "type": "BUG"
    }
  ]
}
 */