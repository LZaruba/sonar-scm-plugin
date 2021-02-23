FROM sonarqube:8-community

COPY target/sonar-scm-plugin-0.1.0-SNAPSHOT.jar /opt/sonarqube/extensions/plugins/
COPY src/test/resources/docker/sonar.properties /opt/sonarqube/conf/