FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} tesla-inventory-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/tesla-inventory-service-0.0.1-SNAPSHOT.jar"]
