# Use the official OpenJDK image as a base image
FROM openjdk:latest

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the JAR file from the target directory into the container
COPY target/weathercommandline-0.0.2.jar /usr/src/app/app.jar

# Define environment variables
ENV API_KEY=""
ENV CITY_SEARCH=""

# Run the Java application
CMD ["java", "-jar", "app.jar"]
