FROM openjdk:15-oraclelinux8
COPY target/libs /app/libs
COPY target/composition-service.jar /app/libs/
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-cp","app:app/libs/*","com.klangfang.core.Application"]