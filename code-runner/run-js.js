// /app/run-js.js

// Decode env variables
const code = Buffer.from(process.env.CODE_B64 || "", "base64").toString("utf8");
const input = Buffer.from(process.env.INPUT_B64 || "", "base64").toString(
  "utf8"
);

// Prepare input reader
const inputLines = input.split("\n");
let currentLine = 0;
const readline = () => inputLines[currentLine++] || "";

// Wrap code in a function and run
try {
  const userFunc = new Function("readline", code);
  const result = userFunc(readline);
  if (result !== undefined) console.log(result);
} catch (err) {
  console.error(err.toString());
}
