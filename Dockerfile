FROM eclipse-temurin:17-jdk

# 빌드된 JAR 복사
COPY build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

