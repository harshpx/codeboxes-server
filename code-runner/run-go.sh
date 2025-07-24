#!/bin/sh

# Decode base64-encoded code and input
echo "$CODE_B64" | base64 -d > main.go
echo "$INPUT_B64" | base64 -d > input.txt

# Compile Go code
go build -o main.out main.go 2> compile_errors.txt
COMPILE_STATUS=$?

# If compilation failed, print the error and exit
if [ $COMPILE_STATUS -ne 0 ]; then
  cat compile_errors.txt 1>&2
  exit 1
fi

# Run the compiled program with input redirection (with timeout)
timeout 5s ./main.out < input.txt
