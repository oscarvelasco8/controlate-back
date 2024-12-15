FROM openjdk:23-jdk-slim
ARG JAR_FILE=target/controlate-0.0.1.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]