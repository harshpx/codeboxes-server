## Codeboxes Server
This server execute code snippets in various programming languages and in a secure and isolated environment.

### Languages Supported till now
- JavaScript
- Python
- C++ & C
- Java
- Golang

### To run locally
**Requirements:* Docker

> bash
```
# Clone the repository
git clone https://github.com/harshpx/codeboxes-server.git
cd codeboxes-server

# Build code-runner container
cd code-runner
docker build -t code-runner .

cd ..

# Build codeboxes-server container
cd server
docker build -t codeboxes-server .

# Run codeboxes-server container
docker run -v /var/run/docker.sock:/var/run/docker.sock -p 8080:8080 codeboxes-server
```

### API Documentation
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