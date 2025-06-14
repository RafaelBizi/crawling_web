FROM maven:3.8.4

ADD . /usr/src/crawling
WORKDIR /usr/src/crawling
EXPOSE 4567
ENTRYPOINT ["mvn", "clean", "verify", "exec:java"]