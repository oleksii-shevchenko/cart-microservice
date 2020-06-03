FROM openjdk:11-jdk-slim

VOLUME /tmp

ARG PASS
ARG PROJECT

COPY build/libs/*.jar cart-microservice.jar
COPY google.json google-credentials.json
COPY cassandra.zip cassandra-credentials.zip

ENV GCP_PROJECT=${PROJECT}
ENV CASSANDRA_CREDENTIALS='/cassandra-credentials.zip'
ENV GOOGLE_APPLICATION_CREDENTIALS='/google-credentials.json'
ENV CASSANDRA_PASSWORD=${PASS}

ENTRYPOINT ["java","-jar","/cart-microservice.jar"]
