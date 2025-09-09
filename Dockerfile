FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=builder /build/target/bank-rest-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8060

ENV JAVA_OPTS="-Xms512m -Xmx1024m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]