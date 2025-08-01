package com.codeboxes.server.Collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  private String id;

  @NotNull
  @NotBlank
  @Indexed(unique = true)
  private String username;

  @NotNull
  @NotBlank
  @Indexed(unique = true)
  private String email;

  @NotNull
  @NotBlank
  private String password;

  @NotNull
  @NotBlank
  private String dp;

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.dp = "https://i.imgur.com/8GO2mo5.png";
  }

  public User(String username, String email, String password, String dp) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.dp = dp;
  }
}
