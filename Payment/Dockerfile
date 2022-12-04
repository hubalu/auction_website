FROM alpine:3.16

# Install maven
RUN apk update
RUN apk add maven
RUN apk add net-tools

WORKDIR /code

# Prepare by downloading dependencies
ADD src /code/src
ADD target /code/target
ADD pom.xml /code/pom.xml

CMD java -cp target/Payment-1.0-SNAPSHOT.jar app.Application
