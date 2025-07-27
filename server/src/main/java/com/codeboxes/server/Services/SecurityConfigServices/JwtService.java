package com.codeboxes.server.Services.SecurityConfigServices;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.codeboxes.server.Collections.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
  @Value("${jwt.secret}")
  private String jwtSecret;

  private SecretKey getKey() {
    byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(User user) {
    return Jwts.builder()
        .subject(user.getUsername())
        .claim("id", user.getId())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
        .signWith(getKey())
        .compact();
  }

  private Claims extractClaims(String token) {
    return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
  }

  public String extractUsername(String token) {
    return extractClaims(token).getSubject();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    String extractedUsername = extractUsername(token);
    Date expiration = extractClaims(token).getExpiration();
    boolean isTokenExpired = expiration.before(new Date());
    return extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired;
  }
}
