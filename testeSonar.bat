rmdir /s /q target
mvn clean verify sonar:sonar ^
  -Dsonar.organization=leticiacarvalho04 ^
  -Dsonar.projectKey=leticiacarvalho04_spring-rest-data-security ^
  -Dsonar.sources=src/main/java ^
  -Dsonar.tests=src/test/java ^
  -Dsonar.host.url=https://sonarcloud.io ^
  -Dsonar.login=51c7982877fa722463f497f4414ce0fcddb1ac43 ^
  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
