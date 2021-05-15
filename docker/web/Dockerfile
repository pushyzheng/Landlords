FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp

ADD landlords-1.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]