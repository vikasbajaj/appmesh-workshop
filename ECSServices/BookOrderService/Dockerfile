FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} BookOrderService.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","/BookOrderService.jar"]