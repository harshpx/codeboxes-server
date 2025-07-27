package com.codeboxes.server.Collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Code {
  @Id
  private String id;

  @NotNull
  @NotBlank
  private String createdBy;

  @NotNull
  @NotBlank
  private String code;

  @NotNull
  @NotBlank
  private String language;
  private String input;

  public Code(String createdBy, String code, String language) {
    this.code = code;
    this.language = language;
  }

  public Code(String createdBy, String code, String language, String input) {
    this.createdBy = createdBy;
    this.code = code;
    this.language = language;
    this.input = input;
  }
}
