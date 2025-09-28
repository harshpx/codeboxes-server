package com.codeboxes.server.DTOs.Auth;

import com.mongodb.lang.NonNull;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OTPRequest {
  @NonNull
  @NotBlank
  private String email;
}
