[![CircleCI](https://circleci.com/gh/LZaruba/sonar-scm-plugin.svg?style=shield)](<https://app.circleci.com/pipelines/github/LZaruba/sonar-scm-plugin>)
# sonar-scm-plugin

## Usage
```bash
$ mvn sonar:sonar -Dsonar.projectKey=[PROJECT_KEY] -Dsonar.host.url=[SONAR_URL] -Dsonar.analysis.host=[REPO_HOST] -Dsonar.analysis.owner=[REPO_OWNER] -Dsonar.analysis.repo=[REPO_NAME] -Dsonar.analysis.pullNumber=[PR_NUMBER] -Dsonar.analysis.token=[GITHUB_ACCESS_TOKEN] -Dsonar.analysis.scm=[SCM] -Dsonar.analysis.username=[SCM_USERNAME] -Dsonar.login=[PROJECT_TOKEN]
```

### Parameters
| Parameter | Example | Description |
| --- | --- | --- |
| `sonar.projectKey` 1/ | test | Key of the SonarQube project |
| `sonar.host.url` 1/ | http://localhost:9000 | URL running the SonarQube instance |
| `sonar.login` 1/ | [PROJECT_TOKEN] | Token generated in SonarQube to access given project |
| `sonar.analysis.host` | https://api.github.com | Hostname of the analyzed SCM |
| `sonar.analysis.owner` | LZaruba | Owner of the repository, usually a part of the repo URL |
| `sonar.analysis.repo` | sonar-scm-plugin | Name of the repository |
| `sonar.analysis.pullNumber` | 27 | #id of the pull request |
| `sonar.analysis.token` | [ACCESS_TOKEN] | Access token for the SCM |
| `sonar.analysis.scm` | github | Type of the SCM, valid values are `github`, `bitbucketServer` |
| `sonar.analysis.username` | LZaruba | Username related to the access token for the SCM |

1/ Properties related to the Maven SonarQube plugin, listed for reference only

## Development and Contributing

### Run Locally

1. Build docker image:

```bash
$ make build
```

2. Run sonarqube locally:

```bash
$ docker-compose up
# or to avoid attaching the volume use:
$ docker run -p 9000:9000 -p 8000:8000 docker.io/lz/sonar-scm-plugin
```

3. Init the SonarQube server
   1. Visit: [http://localhost:9000](http://localhost:9000) 
   2. Login using admin / admin credentials
   3. Update the admin password to any that you like
   4. Accept risk of installed plugin
   5. Create project using the `Manually` option, choose whatever name and key
   6. Create token using `Locally` option when asked about: How do you want to analyze your repository?
   7. Choose any name for the token
   8. Copy the generated token value
   9. Boom! Done!

4. Execute the Sonar analysis as described in the Usage section

### Debugging
1. Attach Remote Debug to `localhost:8000` (see [IntelliJ Tutorial](https://www.jetbrains.com/help/idea/tutorial-remote-debug.html]))
2. Set your breakpoints
3. Execute Sonar analysis as described in the Usage section
4. Wait for a bit once the analyzers are executed in SonarQube
5. Debug...

### Guidelines

* Follow the [Google code style guide](https://google.github.io/styleguide/javaguide.html).
* Make sure that your change contain proper test coverage
* Separate API (Interface) from implementation (Class Implementation)
* Add license header and class header based on the example of `AnalysisProcessor` class
