FROM openjdk:11.0.6-jdk-slim

ARG APP_DIR
ARG APP_JAR
ARG ZOOKEEPER_CONNECT_STR

# Adding a wait-for script for the Zookeeper container
ENV WAIT_VERSION 2.7.3
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /opt/wait
RUN chmod +x /opt/wait

WORKDIR /app
ADD ${APP_DIR}/${APP_JAR} /app

ENV APP_JAR=${APP_JAR}
ENV ZOOKEEPER_CONNECT_STR=${ZOOKEEPER_CONNECT_STR}
CMD /bin/bash -c "/opt/wait && java -jar $APP_JAR --spring.cloud.zookeeper.connect\-string=$ZOOKEEPER_CONNECT_STR"
