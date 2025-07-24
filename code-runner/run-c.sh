#!/bin/sh

# Decode base64-encoded code and input
echo "$CODE_B64" | base64 -d > main.c
echo "$INPUT_B64" | base64 -d > input.txt

# Compile and capture any compilation errors
gcc -o main.out main.c 2> compile_errors.txt
COMPILE_STATUS=$?

# If compilation failed, print the error and exit
if [ $COMPILE_STATUS -ne 0 ]; then
  cat compile_errors.txt 1>&2
  exit 1
fi

# Run the compiled program with input redirection
# Timeout after 5 seconds to prevent infinite loops
timeout 5s ./main.out < input.txt
