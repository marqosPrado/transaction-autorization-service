FROM eclipse-temurin:21-jdk-alpine AS builder
LABEL authors="Marcos Prado"

WORKDIR /build

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
LABEL authors="Marcos Prado"

WORKDIR /app

COPY --from=builder /build/target/transaction-autorization-service-0.0.1-SNAPSHOT.jar /app/transaction-autorization-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/transaction-autorization-service.jar"]