FROM maven:3.6-openjdk-8-slim AS MAVEN_TOOL_CHAIN
LABEL maintainer="opensource@klarna.com"

WORKDIR /tmp

COPY . .
RUN mvn dependency:go-offline
RUN mvn verify


FROM sonarqube:8-community

COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar /opt/sonarqube/extensions/plugins/
COPY sonar.properties /opt/sonarqube/conf/
