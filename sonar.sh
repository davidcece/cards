#!/usr/bin/env sh

export JAVA_HOME=/c/Program\ Files/OpenLogic/jdk-17.0.8.7-hotspot

mvn clean verify sonar:sonar \
  -Dsonar.projectKey=cards \
  -Dsonar.projectName='cards' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=sqp_b10f665e0dc0cb4e696bad589e1671be32bdc36d