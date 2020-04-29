FROM openjdk:8-jre-alpine
VOLUME /tmp
EXPOSE 8080
ADD target/ms-sdp-header-decryptor-1.0.0.jar ms-sdp-header-decryptor.jar
CMD /bin/sh -c 'touch /ms-sdp-header-decryptor.jar'
#RUN apk add tzdata
ENV TZ=Africa/Nairobi
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-Xmx384m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom","-jar","/ms-sdp-header-decryptor.jar"]
