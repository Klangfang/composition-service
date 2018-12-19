FROM openjdk:11-jdk
VOLUME /tmp
#ARG JAR_FILE
#ADD ${JAR_FILE} app.jar
COPY target/composition-service-1.0.jar composition-service-1.0.jar
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","composition-service-1.0.jar"]
#CMD ["java","-Djava.security.egd=file:/dev/./urandom",
   # "-Dspring.profiles.active=prod","-jar","composition-service-1.0.jar"]
