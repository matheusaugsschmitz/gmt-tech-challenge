FROM openjdk:24

# Creating folder to store JAR file
WORKDIR /app

# Copying JAR file
COPY build/libs/gmt-tech-challenge-0.0.1-SNAPSHOT-plain.jar /app/gmt-tech-challenge.jar

# Expose port 8080 for HTTP traffic
EXPOSE 8080

# Runing the application
ENTRYPOINT ["java", "-jar", "gmt-tech-challenge.jar"]