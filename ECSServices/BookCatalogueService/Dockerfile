FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} BookCatalaogueService.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/BookCatalaogueService.jar"]