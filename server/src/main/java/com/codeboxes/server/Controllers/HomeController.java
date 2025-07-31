package com.codeboxes.server.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeboxes.server.DTOs.CommonResponse;

@RestController
public class HomeController {
  // ---------------------- Public Endpoints ----------------------
  @GetMapping("/")
  public ResponseEntity<CommonResponse<String>> home() {
    CommonResponse<String> response = new CommonResponse<>("Codeboxes API is running!");
    return ResponseEntity.ok(response);
  }
}
