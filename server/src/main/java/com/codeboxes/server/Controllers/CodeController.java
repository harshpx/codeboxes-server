package com.codeboxes.server.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeboxes.server.Collections.Code;
import com.codeboxes.server.DTOs.CommonResponse;
import com.codeboxes.server.DTOs.Code.SaveCodeRequest;
import com.codeboxes.server.Services.CodeService;
import com.codeboxes.server.Services.SecurityConfigServices.UserDetailsImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/codes")
public class CodeController {
  @Autowired
  private CodeService service;

  // ---------------------- Authorized Endpoints ----------------------
  @GetMapping
  public ResponseEntity<CommonResponse<List<Code>>> getCodesByUser(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<Code> codes = service.getCodesByUser(userDetails);
    CommonResponse<List<Code>> response = new CommonResponse<>(codes);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{codeId}")
  public ResponseEntity<CommonResponse<Code>> getCodeById(@PathVariable String codeId) {
    Code code = service.getCodeById(codeId);
    CommonResponse<Code> response = new CommonResponse<>(code);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CommonResponse<Code>> createCode(@Valid @RequestBody SaveCodeRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Code createdCode = service.saveCode(request, userDetails);
    CommonResponse<Code> response = new CommonResponse<>(createdCode);
    return ResponseEntity.ok(response);
  }

  @PutMapping
  public ResponseEntity<CommonResponse<Code>> updateCode(@Valid @RequestBody SaveCodeRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    if (request.getId() == null || request.getId().isBlank()) {
      throw new IllegalArgumentException("Code with an Id must be provided for update");
    }
    Code createdCode = service.saveCode(request, userDetails);
    CommonResponse<Code> response = new CommonResponse<>(createdCode);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{codeId}")
  public ResponseEntity<CommonResponse<String>> deleteCode(@PathVariable String codeId) {
    service.deleteCode(codeId);
    CommonResponse<String> response = new CommonResponse<>("Code deleted successfully");
    return ResponseEntity.ok(response);
  }
}
