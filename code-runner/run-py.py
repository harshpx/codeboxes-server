import os
import base64
import sys
import io

code_b64 = os.environ.get("CODE_B64", "")
input_b64 = os.environ.get("INPUT_B64", "")

code = base64.b64decode(code_b64).decode("utf-8")
input_data = base64.b64decode(input_b64).decode("utf-8")

sys.stdin = io.StringIO(input_data)

try:
  exec(code)
except Exception as e:
  print("Runtime Error:", e)
