FROM mongo:latest

#ENV MONGO_INITDB_DATABASE="LoginService"
# Install maven
RUN apt-get update
RUN apt-get install -y maven
RUN apt-get install -y net-tools
RUN apt-get install -y procps

WORKDIR /code

# Prepare by downloading dependencies
ADD src /code/src
ADD pom.xml /code/pom.xml
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait /wait
RUN mvn dependency:resolve
RUN mvn verify clean --fail-never

# Adding source, compile and package into a fat jar
RUN mvn clean package

#EXPOSE 4567
CMD /wait && java -cp target/Auction-1.0-SNAPSHOT.jar app.Application