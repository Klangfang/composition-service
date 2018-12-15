FROM openjdk:11-jdk
VOLUME /tmp
#ARG JAR_FILE
#ADD ${JAR_FILE} app.jar
#CMD ["java","-jar","app.jar"]
COPY build/libs/composition-service-0.0.1.jar composition-service-0.0.1.jar
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","composition-service-0.0.1.jar"]
