FROM mongo:latest

# Install maven
RUN apt-get update
RUN apt-get install -y maven
RUN apt-get install -y net-tools
RUN apt-get install -y procps

WORKDIR /code

# Prepare by downloading dependencies
ADD src /code/src
ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Adding source, compile and package into a fat jar
RUN ["mvn", "package"]

EXPOSE 4567
#CMD ["java", "-cp", "target/Auction-1.0-SNAPSHOT.jar", "app.Application"]