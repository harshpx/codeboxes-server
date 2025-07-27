package com.codeboxes.server.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.codeboxes.server.Services.CodeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/codes")
public class CodeController {
  @Autowired
  private CodeService service;

  @GetMapping("/user/{userId}")
  public ResponseEntity<CommonResponse<List<Code>>> getCodesByUser(@PathVariable String userId) {
    List<Code> codes = service.getCodesByUser(userId);
    CommonResponse<List<Code>> response = new CommonResponse<>(codes);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CommonResponse<Code>> createCode(@Valid @RequestBody Code code) {
    Code createdCode = service.saveCode(code);
    CommonResponse<Code> response = new CommonResponse<>(createdCode);
    return ResponseEntity.ok(response);
  }

  @PutMapping
  public ResponseEntity<CommonResponse<Code>> updateCode(@Valid @RequestBody Code code) {
    Code createdCode = service.saveCode(code);
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
