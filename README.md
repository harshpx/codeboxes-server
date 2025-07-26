# Codeboxes Server
### A container-based code execution API that allows you to run code in various programming languages.

It is designed to be used with the [Codeboxes (https://www.codeboxes.in)](https://www.codeboxes.in) web application, but can also be used independently

Deployed API url: [https://codeboxes.152.42.158.94.nip.io](https://codeboxes.152.42.158.94.nip.io)

* **NOTE:** The deployed API only allows requests from `https://www.codeboxes.in` and `http://localhost:3000` to prevent misuse.
* Build locally & modify the CORS settings in `server/src/main/java/com/codeboxes/server/Configs/CorsConfig.java` to allow requests from other origins.

## Supported Languages
* Java
* JavaScript 
* Python
* C++
* C
* Golang

## API Documentation
```
POST: /api/v1/execute
Payload:
{
  "code": "function main() {\n    let str = readline();\n    console.log(\"Input string: \" + str);\n}\n    \nmain();",
  "language": "js",
  "input": "temp string input"
}
Response:
{
    "status": 200,
    "timeStamp": 1753405366504,
    "success": true,
    "response": {
        "output": "Input string: temp string input\n",
        "error": false
    }
}
```

## To run locally
* **Linux / MacOS**
  * Requirements: **Docker** (Docker Desktop or Docker CLI with Docker Daemon)
  * Clone the repository & navigate to the project directory
    ```
    git clone https://github.com/harshpx/codeboxes-server.git
    cd codeboxes-server
    ```
  * Build a containers with names `code-runner` &  `codeboxes-server` from Dockerfiles inside respective directories
    ```
    docker build -t code-runner ./code-runner
    docker build -t codeboxes-server ./server
    ```
  * Run codeboxes-server container
    ```
    docker run \
        --name codeboxes-server \
        -v /var/run/docker.sock:/var/run/docker.sock \
        -p 8080:8080 codeboxes-server
    ```
* **Windows**
  * Requirements: **Docker Desktop**, **JDK 21**, **Maven**
  * Clone the repository & navigate to the project directory
    ```
    git clone https://github.com/harshpx/codeboxes-server.git
    cd codeboxes-server
    ```
  * Build a container with name `code-runner` from Dockerfile inside code-runner directory
    ```
    docker build -t code-runner ./code-runner
    ```
  * Navigate to the server directory & run spring-boot application (make sure port 8080 is free)
    ```
    cd server
    mvn clean install
    mvn spring-boot:run
    ```