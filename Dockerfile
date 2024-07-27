# Use an official Gradle image with JDK 21 to run Gradle tasks
FROM gradle:latest as build

# Set the working directory inside the container
WORKDIR /home/gradle/project

# Copy the entire project to the working directory
COPY . .

# Run the Gradle build task
RUN gradle clean bootJar

# Use an official OpenJDK 21 image to run the application
FROM eclipse-temurin:21-jre-alpine

# Copy the built jar from the build stage
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]

