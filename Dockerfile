# Dockerfile para ms-authentication (Spring Boot + Java 23)

# Etapa 1: Build
FROM eclipse-temurin:23-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# Etapa 2: Imagen final
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]

