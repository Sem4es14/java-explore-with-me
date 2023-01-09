FROM amazoncorretto:11
COPY target/*.jar ewm-main-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/ewm-main-service.jar"]