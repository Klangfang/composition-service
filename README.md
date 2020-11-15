# Composition Service
____________________________________________________________________________________________________________________________________________________________________

Welcome to the composition service - the application core system of the klangfang app.<br>
It provides many functionalities, such as:
- saving a composition
- collaborating with a composition
- listing all available compositions.
...<br>
The klangfang android app is communicating directly with the composition service via rest api.<br>
The service is actually hosted in heroku.<br>
<br>
<br>
This project is based on the Spring Boot Framework and Java.<br>
<br>
To build this project and run the application without the docker container (i.e. in the host OS):<br>
````
./mvnw package && java -jar target/composition-service-1.0.jar

````
Building a new tagged docker image:
````
./mvnw install dockerfile:build

````

Running the tagged docker image:
````
docker run -p 8080:8080 -t casasky/composition-service

````

