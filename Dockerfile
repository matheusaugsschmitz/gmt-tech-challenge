FROM openjdk:24

# Creating folder to store JAR file
WORKDIR /app

# Copying JAR file
COPY target/gmt-tech-challenge.jar /app/gmt-tech-challenge.jar

# Expose port 8080 for HTTP traffic
EXPOSE 8080

# Runing the application
ENTRYPOINT ["java", "-jar", "gmt-tech-challenge.jar"]