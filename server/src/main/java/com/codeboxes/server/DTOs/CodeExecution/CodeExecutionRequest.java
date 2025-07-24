package com.codeboxes.server.DTOs.CodeExecution;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeExecutionRequest {
  @NotNull
  public String code;
  @NotNull
  public String language;
  @NotNull
  public String input;

  public CodeExecutionRequest() {
  }

  public CodeExecutionRequest(String code, String language, String input) {
    this.code = code;
    this.language = language;
    this.input = input;
  }
}
