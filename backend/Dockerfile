FROM maven:3.9.6-eclipse-temurin-21 AS deps
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

FROM deps AS builder
WORKDIR /app

COPY libs/ ./libs/
COPY src ./src
RUN mvn clean package -DskipTests -B

RUN java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

RUN groupadd --system appgroup && useradd --system --gid appgroup appuser
COPY --from=builder /app/target/extracted/dependencies/ ./
COPY --from=builder /app/target/extracted/spring-boot-loader/ ./
COPY --from=builder /app/target/extracted/snapshot-dependencies/ ./
COPY --from=builder /app/target/extracted/application/ ./

USER appuser
EXPOSE 8080


ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "org.springframework.boot.loader.launch.JarLauncher"]
