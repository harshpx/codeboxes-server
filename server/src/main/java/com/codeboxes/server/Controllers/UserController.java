package com.codeboxes.server.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeboxes.server.DTOs.CommonResponse;
import com.codeboxes.server.DTOs.Auth.AuthenticatedUserResponse;
import com.codeboxes.server.DTOs.Auth.LoginUserRequest;
import com.codeboxes.server.DTOs.Auth.RegisterUserRequest;
import com.codeboxes.server.DTOs.Auth.PatchUserRequest;
import com.codeboxes.server.Services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  @Autowired
  private UserService service;

  @PostMapping("/register")
  public ResponseEntity<CommonResponse<AuthenticatedUserResponse>> registerUser(
      @Valid @RequestBody RegisterUserRequest request) {
    AuthenticatedUserResponse response = service.registerUser(request);
    return ResponseEntity.ok(new CommonResponse<>(response));
  }

  @PostMapping("/login")
  public ResponseEntity<CommonResponse<AuthenticatedUserResponse>> loginUser(
      @Valid @RequestBody LoginUserRequest request) {
    AuthenticatedUserResponse response = service.authenticateUser(request);
    return ResponseEntity.ok(new CommonResponse<>(response));
  }

  @PatchMapping("/{userId}")
  public ResponseEntity<CommonResponse<AuthenticatedUserResponse>> patchUpdateUser(
      @RequestBody PatchUserRequest request,
      @PathVariable String userId) {
    AuthenticatedUserResponse rawdata = service.patchUpdateUser(request, userId);
    CommonResponse<AuthenticatedUserResponse> response = new CommonResponse<>(rawdata);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<CommonResponse<String>> deleteUser(@PathVariable String userId) {
    service.deleteUser(userId);
    CommonResponse<String> response = new CommonResponse<>("User deleted successfully");
    return ResponseEntity.ok(response);
  }
}
