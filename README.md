# Salary Management

<b>How to build, test, and run:</b><br/>
1. Download this sourcecodes and cd to salarymgmt root folder
2. Run command "mvn clean test" to run junit test cases, make sure BUILD SUCCESS
3. Run command "mvn package" to build jar file, make sure BUILD SUCCESS
4. Run command "docker build -t salarymgmt ." to build docker image
5. Run command "docker run -d salarymgmt" to run docker image
(alternatively, you may pull the image from docker hub "docker pull jiangwensi/salarymgmt:latest" and run "docker run -d jiangwensi/salarymgmt")
6. Post requests to the below links for testing <br/>
(sample postman script: https://www.postman.com/collections/4714e71e3779aa2bfc18)
    - POST http://localhost:8080/users
    - PATCH http://localhost:8080/users/emp0001
    - DELETE http://localhost:8080/users/e0001
    - GET http://localhost:8080/users/e0001
    - GET http://localhost:8080/users
    - POST http://localhost:8080/users/upload

