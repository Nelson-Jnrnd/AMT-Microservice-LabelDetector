FROM amazoncorretto:17.0.5-alpine3.16

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src
EXPOSE 8081

CMD ["./mvnw", "spring-boot:run"]
