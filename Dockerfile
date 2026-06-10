FROM eclipse-temurin:21-jdk

# 빌드된 JAR 복사 (Maven은 target 폴더에 생성됨)
COPY target/*.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]