package com.codeboxes.server.Controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeboxes.server.DTOs.CommonResponse;
import com.codeboxes.server.DTOs.CodeExecution.CodeExecutionRequest;
import com.codeboxes.server.DTOs.CodeExecution.CodeExecutionResponse;
import com.codeboxes.server.Services.CodeExecutionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/execute")
public class CodeExecutionController {
  @Autowired
  private CodeExecutionService codeExecutionService;

  @PostMapping
  public ResponseEntity<CommonResponse<CodeExecutionResponse>> executeCode(
      @Valid @RequestBody CodeExecutionRequest request)
      throws IOException, InterruptedException {
    CodeExecutionResponse executionResponse = codeExecutionService.executeCode(request);
    CommonResponse<CodeExecutionResponse> response = new CommonResponse<>(executionResponse);
    return ResponseEntity.ok(response);
  }
}
