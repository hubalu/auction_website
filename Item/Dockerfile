FROM postgres:latest

# Install maven
RUN apt-get update
RUN apt-get install -y maven
RUN apt-get install -y net-tools
RUN apt-get install -y procps

ENV POSTGRES_PASSWORD="password"

WORKDIR /code

# Prepare by downloading dependencies
ADD src /code/src
ADD postgresql-42.2.8.jar /code
ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Adding source, compile and package into a fat jar
RUN ["mvn", "package"]

#CMD ["java", "-cp", "target/frontend-1.0-SNAPSHOT.jar", "app.Application"]
