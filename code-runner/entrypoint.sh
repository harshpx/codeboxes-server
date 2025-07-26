#!/bin/bash

# Fail on any error
set -e

# # Print current language
# echo "Executing for language: $LANGUAGE"

# Route based on LANGUAGE
if [ "$LANGUAGE" = "js" ]; then
  node /app/run-js.js

elif [ "$LANGUAGE" = "py" ]; then
  python3 /app/run-py.py

elif [ "$LANGUAGE" = "java" ]; then
  bash /app/run-java.sh

elif [ "$LANGUAGE" = "cpp" ]; then
  bash /app/run-cpp.sh

elif [ "$LANGUAGE" = "c" ]; then
  bash /app/run-c.sh

elif [ "$LANGUAGE" = "go" ]; then
  bash /app/run-go.sh

else
  echo "Unsupported language: $LANGUAGE" >&2
  exit 1
fi