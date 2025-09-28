package com.codeboxes.server.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeboxes.server.DTOs.CommonResponse;
import com.codeboxes.server.DTOs.Auth.AuthenticatedUserResponse;
import com.codeboxes.server.DTOs.Auth.LoginUserRequest;
import com.codeboxes.server.DTOs.Auth.OTPRequest;
import com.codeboxes.server.DTOs.Auth.RegisterUserRequest;
import com.codeboxes.server.DTOs.Auth.PatchUserRequest;
import com.codeboxes.server.Services.UserService;
import com.codeboxes.server.Services.SecurityConfigServices.UserDetailsImpl;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
  @Autowired
  private UserService service;

  // ---------------------- Public Endpoints ----------------------
  @PostMapping("/otp")
  public ResponseEntity<CommonResponse<String>> sendOTP(@Valid @RequestBody OTPRequest request)
      throws MessagingException {
    service.sendOTP(request);
    return ResponseEntity.ok(new CommonResponse<>("OTP sent successfully"));
  }

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

  @GetMapping("/check-availability")
  public ResponseEntity<CommonResponse<Boolean>> checkUsernameAvailability(
      @RequestParam(required = false) String username, @RequestParam(required = false) String email) {
    boolean isEmailEmpty = email == null || email.isBlank();
    boolean isUsernameEmpty = username == null || username.isBlank();
    if (!isUsernameEmpty && !isEmailEmpty) {
      boolean isPairAvailable = service.checkEmailAvailability(email) && service.checkUsernameAvailability(username);
      return ResponseEntity.ok(new CommonResponse<>(isPairAvailable));
    } else if (!isUsernameEmpty) {
      return ResponseEntity.ok(new CommonResponse<>(service.checkUsernameAvailability(username)));
    } else if (!isEmailEmpty) {
      return ResponseEntity.ok(new CommonResponse<>(service.checkEmailAvailability(email)));
    }
    throw new IllegalArgumentException("Either username or email must be provided");
  }

  // ---------------------- Authorized Endpoints ----------------------
  @GetMapping
  public ResponseEntity<CommonResponse<AuthenticatedUserResponse>> getAuthorizedUser(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    AuthenticatedUserResponse response = service.getAuthorizedUser(userDetails);
    return ResponseEntity.ok(new CommonResponse<>(response));
  }

  @PatchMapping
  public ResponseEntity<CommonResponse<AuthenticatedUserResponse>> patchUpdateUser(
      @RequestBody PatchUserRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    AuthenticatedUserResponse rawdata = service.patchUpdateUser(request, userDetails);
    CommonResponse<AuthenticatedUserResponse> response = new CommonResponse<>(rawdata);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping
  public ResponseEntity<CommonResponse<String>> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    service.deleteUser(userDetails);
    CommonResponse<String> response = new CommonResponse<>("User deleted successfully");
    return ResponseEntity.ok(response);
  }
}
