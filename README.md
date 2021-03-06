[![CircleCI](https://circleci.com/gh/LZaruba/sonar-scm-plugin.svg?style=shield)](<https://app.circleci.com/pipelines/github/LZaruba/sonar-scm-plugin>)
# sonar-scm-plugin

## Usage

### GitHub

```bash
$ mvn sonar:sonar -Dsonar.projectKey=test -Dsonar.host.url=http://localhost:9000 -Dsonar.analysis.host=https://api.github.com -Dsonar.analysis.owner=LZaruba -Dsonar.analysis.repo=sonar-scm-plugin -Dsonar.analysis.pullNumber=[PR_NUMBER] -Dsonar.analysis.token=[GITHUB_TOKEN] -Dsonar.analysis.scm=github -Dsonar.analysis.username=[GITHUB_USERNAME] -Dsonar.login=[TOKEN]
```

### Docker

build docker image:

```bash
$ make build
```

run sonarqube locally:

```bash
$ docker-compose up
```

Then visit: [http://localhost:9000](http://localhost:9000) (admin / admin)


## Contributing

* Follow the [Google code style guide](https://google.github.io/styleguide/javaguide.html).
* Make sure that your change contain proper test coverage
* Separate API (Interface) from implementation (Class Implementation)
* Add license header and class header based on the example of AnalysisProcessor class
