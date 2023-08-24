#!/usr/bin/env sh

export JAVA_HOME=/c/Program\ Files/OpenLogic/jdk-17.0.8.7-hotspot

mvn clean verify sonar:sonar \
  -Dsonar.projectKey=cards \
  -Dsonar.projectName='cards' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_15aab95ceca4ec0111cb45602f811b6b7aa2e6cb