FROM openjdk:11
COPY target/*.jar ewm_service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/ewm_main_service.jar"]