package com.codeboxes.server.DTOs.Auth;

import com.codeboxes.server.Collections.User;

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
  private String dp;
  private String token;

  public AuthenticatedUserResponse(User user, String token) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.dp = user.getDp();
    this.token = token;
  }
}
