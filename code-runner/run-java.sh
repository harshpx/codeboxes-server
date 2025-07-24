#!/bin/sh

# Decode base64 environment variables
echo "$CODE_B64" | base64 -d > Main.java
echo "$INPUT_B64" | base64 -d > input.txt

# Compile
javac Main.java 2> compile_errors.txt
COMPILE_STATUS=$?

# If compile failed, print errors and exit
if [ $COMPILE_STATUS -ne 0 ]; then
  cat compile_errors.txt 1>&2
  exit 1
fi

# Run the program with redirected input
timeout 5s java Main < input.txt
