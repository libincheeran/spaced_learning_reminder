FROM eclipse-temurin:21-jdk as build
WORKDIR /app
COPY . .
# Make gradlew executable and run build without tests
RUN chmod +x ./gradlew && \
    ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 