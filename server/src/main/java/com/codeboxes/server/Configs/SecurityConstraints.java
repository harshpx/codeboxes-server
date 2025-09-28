package com.codeboxes.server.Configs;

public class SecurityConstraints {
  public static final String[] PUBLIC_ENDPOINTS = {
      "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs.yaml",
      "/", "/api/v1/execute",
      "/api/v1/users/otp", "/api/v1/users/register",
      "/api/v1/users/login", "/api/v1/users/check-availability",
  };
}
