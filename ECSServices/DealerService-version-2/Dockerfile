FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} EmailNotificationService.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","/EmailNotificationService.jar"]