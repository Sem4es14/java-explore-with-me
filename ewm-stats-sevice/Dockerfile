FROM openjdk:11
COPY target/*.jar ewm_stats.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","/ewm_stats_service.jar"]