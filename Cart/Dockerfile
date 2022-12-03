FROM ubuntu/mysql
# ENV MYSQL_USER="root"
# ENV MYSQL_ROOT_PASSWORD="secretpass"
# ENV MYSQL_DATABASE="userData"
# Install maven
RUN apt-get -y update
RUN apt-get -y install maven
RUN apt-get -y install net-tools

WORKDIR /code

# Prepare by downloading dependencies
ADD src /code/src
ADD target /code/target
ADD pom.xml /code/pom.xml
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait /wait
# RUN mvn dependency:resolve
# RUN mvn verify clean --fail-never
RUN chmod +x /wait

# Adding source, compile and package into a fat jar
# RUN mvn clean package

CMD /wait && java -cp target/Cart-1.0-SNAPSHOT.jar app.Application