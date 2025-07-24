package com.codeboxes.server.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.codeboxes.server.DTOs.CodeExecution.CodeExecutionRequest;
import com.codeboxes.server.DTOs.CodeExecution.CodeExecutionResponse;

@Service
public class CodeExecutionService {
  public CodeExecutionResponse executeCode(CodeExecutionRequest request) throws IOException, InterruptedException {
    // validate request
    if (request.getCode() == null || request.getLanguage() == null) {
      throw new RuntimeException("Invalid request: Check payload");
    }

    // encode code and input into base64
    String encodedCode = Base64.getEncoder().encodeToString(request.getCode().getBytes());
    String encodedInput = Base64.getEncoder()
        .encodeToString(request.getInput() != null ? request.getInput().getBytes() : new byte[0]);

    // build code-runner docker container process
    ProcessBuilder processBuilder = new ProcessBuilder(
        "docker", "run", "--rm",
        "-e", "LANGUAGE=" + request.getLanguage(),
        "-e", "CODE_B64=" + encodedCode,
        "-e", "INPUT_B64=" + encodedInput,
        "code-runner:latest");

    // start the process
    Process process = processBuilder.start();

    // read output and error streams
    BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    StringBuilder outputSb = new StringBuilder();
    String line;
    while ((line = outputReader.readLine()) != null) {
      outputSb.append(line).append("\n");
    }
    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    StringBuilder errorSb = new StringBuilder();
    while ((line = errorReader.readLine()) != null) {
      errorSb.append(line).append("\n");
    }

    // exit process
    process.waitFor();

    // set output and error in response
    String outputString = "";
    if (errorSb.length() > 0) {
      outputString = errorSb.toString();
      return new CodeExecutionResponse(outputString, true);
    } else {
      outputString = outputSb.toString();
      return new CodeExecutionResponse(outputString, false);
    }
  }
}
