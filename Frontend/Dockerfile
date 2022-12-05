FROM mongo:latest

# Install maven
RUN apt-get update
RUN apt-get install -y maven
RUN apt-get install -y net-tools
RUN apt-get install -y procps

# ENV MONGO_INITDB_DATABASE="LoginService"
WORKDIR /code

# Prepare by downloading dependencies
ADD src /code/src
ADD target /code/target
ADD pom.xml /code/pom.xml
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait /wait
RUN chmod +x /wait

CMD /wait && java -cp target/frontend-1.0-SNAPSHOT.jar app.Application
