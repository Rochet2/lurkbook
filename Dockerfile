FROM openjdk:8-jdk-alpine
# FROM docker:latest
# RUN apk add curl
# RUN curl -L "https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
# RUN chmod +x /usr/local/bin/docker-compose
# RUN ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
# RUN docker-compose
# CMD ["docker-compose", "up"]

RUN apk add maven

COPY . /usr/src/myapp
WORKDIR /usr/src/myapp

RUN mvn -DskipTests clean dependency:list install
RUN ls -AhR

EXPOSE 8080
CMD ["java", "-Dspring.profiles.active=test", "-Dserver.port=8080", "-jar", "target/wepa_Projekti-1.0-SNAPSHOT.jar"]

