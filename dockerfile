# Use a multi-stage build to minimize the final image size

# Step 1: Build the application
FROM maven:3.8.5-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml .
COPY src ./src

# Run Maven to build the application
RUN mvn clean package -DskipTests

# Step 2: Create a smaller runtime image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build stage to the runtime image
COPY --from=build /app/target/*.jar app.jar

# Specify the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
