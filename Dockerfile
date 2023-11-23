FROM maven:3.9.5-amazoncorretto-21 AS rest-builder
ENV APP_HOME /opt/app
WORKDIR $APP_HOME
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

# Run
FROM amazoncorretto:17
ENV APP_HOME /opt/app
WORKDIR $APP_HOME
COPY --from=rest-builder $APP_HOME/target/*.jar $APP_HOME/rest_sante.jar

ENTRYPOINT java -jar rest_sante.jar