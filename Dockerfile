FROM openjdk:21-jdk
WORKDIR /app
COPY target/PandevTT-0.0.1-SNAPSHOT.jar PandevTT.jar
CMD ["java", "-jar", "PandevTT.jar"]
