FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} EnquiryService.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/EnquiryService.jar"]