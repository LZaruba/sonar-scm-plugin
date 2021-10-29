FROM maven:3.6-openjdk-11-slim AS MAVEN_TOOL_CHAIN
LABEL maintainer="opensource@klarna.com"

WORKDIR /tmp

COPY . .
RUN mvn dependency:go-offline
RUN mvn clean verify


FROM sonarqube:9-community

COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar /opt/sonarqube/extensions/plugins/
COPY sonar.properties /opt/sonarqube/conf/
