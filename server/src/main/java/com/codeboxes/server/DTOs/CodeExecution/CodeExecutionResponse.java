package com.codeboxes.server.DTOs.CodeExecution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeExecutionResponse {
  private String output;
  private boolean error;

  public CodeExecutionResponse() {
  }

  public CodeExecutionResponse(String output) {
    this.output = output;
    this.error = false;
  }

  public CodeExecutionResponse(String output, boolean error) {
    this.output = output;
    this.error = error;
  }
}
