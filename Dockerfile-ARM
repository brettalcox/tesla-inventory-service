FROM adoptopenjdk/openjdk11:armv7l-debian-jre-11.0.11_9
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} tesla-inventory-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/tesla-inventory-service-0.0.1-SNAPSHOT.jar"]
