FROM rabbitmq:3-alpine

# Install maven
RUN apk update
RUN apk add maven
RUN apk add net-tools

WORKDIR /code

# Prepare by downloading dependencies
ADD src /code/src
ADD pom.xml /code/pom.xml
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait /wait
RUN mvn dependency:resolve
RUN mvn verify clean --fail-never

# Adding source, compile and package into a fat jar
RUN mvn clean package

CMD /wait && java -cp target/Messenger-1.0-SNAPSHOT.jar app.Application
