FROM openjdk:8-jre-alpine

LABEL maintainer = "dotina@safaricom.co.ke"

EXPOSE 8080

VOLUME /tmp


ADD target/ms-sdp-header-decryptor-1.0.1.jar ms-sdp-header-decryptor.jar

RUN /bin/sh -c 'touch /ms-sdp-header-decryptor.jar'

#RUN apk add tzdata
ENV TZ=Africa/Nairobi

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV NO_PROXY="*.safaricom.net,.safaricom.net"

ENTRYPOINT ["java","-Xmx1024m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom","-jar","/ms-sdp-header-decryptor.jar"]