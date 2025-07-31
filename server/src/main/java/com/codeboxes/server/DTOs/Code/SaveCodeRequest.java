package com.codeboxes.server.DTOs.Code;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveCodeRequest {
  private String id;
  @NotNull
  @NotBlank
  private String title;
  @NotNull
  @NotBlank
  private String code;
  @NotNull
  @NotBlank
  private String language;
  @NotNull
  private String input;
}
