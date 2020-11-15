# Composition Service

Welcome to the composition service - the application core system of the klangfang app.
It provides many functionalities, such as:
- saving a composition
- collaborating with a composition
- listing all available compositions
- ...

The klangfang android app is communicating directly with the composition service via rest api.<br>
The service is actually hosted in heroku.<br>
<br>
<br>
This project is based on the Spring Boot Framework and Java.<br>
<br>
To build this project and run the application without the docker container (i.e. in the host OS):<br>
````
mvn clean install && java -jar target/composition-service-1.0.jar

````
Building and Pushing a new tagged docker image:
````
heroku container:push web

````

Releasing the tagged docker image:
````
heroku container:release web

````

