FROM openjdk:11-jdk
VOLUME /tmp
COPY target/composition-service-1.0.jar composition-service-1.0.jar
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
CMD ["java","-Dspring.profiles.active=prod","-jar","composition-service-1.0.jar"]
