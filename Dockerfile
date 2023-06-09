FROM maven:3.8.2-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/time.cutter-1.0.0.jar ./
CMD ["java", "-jar", "time.cutter-1.0.0.jar"]