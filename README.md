# Salary Management

<b>How to build, test, and run:</b><br/>
1. Download this sourcecodes and cd to salarymgmt root folder
2. Run command "<b>mvn clean test</b>" to run junit test cases, make sure BUILD SUCCESS
3. Run command "<b>mvn package</b>" to build jar file, make sure BUILD SUCCESS
4. Run command "<b>docker build -t salarymgmt .</b>" to build docker image
5. Run command "<b>docker run -d salarymgmt</b>" to run docker image<br/>
(alternatively, you may pull the image from docker hub "<b>docker pull jiangwensi/salarymgmt:latest</b>" and run "<b>docker run -d jiangwensi/salarymgmt</b>")
6. Post requests to the below links for testing <br/>(run "docker-machine ip" to check the docker ip address if it is not localhost") <br/>
(sample postman script: https://www.postman.com/collections/4714e71e3779aa2bfc18)
    - POST http://localhost:8080/users/upload
    - GET http://localhost:8080/users
    - POST http://localhost:8080/users
    - PATCH http://localhost:8080/users/emp0001
    - GET http://localhost:8080/users/e0001
    - DELETE http://localhost:8080/users/e0001


