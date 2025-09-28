package com.codeboxes.server.DTOs.Auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {
  @NotNull
  @NotBlank
  private String username;
  @NotNull
  @NotBlank
  private String email;
  @NotNull
  @NotBlank
  private String password;
  @NotNull
  @NotBlank
  private String otp;
}
