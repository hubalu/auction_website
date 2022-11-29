FROM ubuntu/mysql
ENV MYSQL_USER="root"
ENV MYSQL_ROOT_PASSWORD="secretpass"
ENV MYSQL_DATABASE="userData"
# Install maven
RUN apt-get -y update
RUN apt-get -y install maven
RUN apt-get -y install net-tools

WORKDIR /code

# Prepare by downloading dependencies
ADD src /code/src
ADD pom.xml /code/pom.xml
RUN mvn dependency:resolve
RUN mvn verify clean --fail-never

# Adding source, compile and package into a fat jar
RUN mvn clean package


CMD ["java", "-cp", "target/User-1.0-SNAPSHOT.jar", "app.Application"]
