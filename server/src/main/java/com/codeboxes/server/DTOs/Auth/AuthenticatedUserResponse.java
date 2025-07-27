package com.codeboxes.server.DTOs.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUserResponse {
  private String id;
  private String username;
  private String email;
  private String token;
}
