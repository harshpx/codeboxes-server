package com.codeboxes.server.Collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.codeboxes.server.DTOs.Code.SaveCodeRequest;

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
  private String title;

  @NotNull
  @NotBlank
  private String code;

  @NotNull
  @NotBlank
  private String language;

  @NotNull
  private String input;

  public Code(String createdBy, String title, String code, String language, String input) {
    this.createdBy = createdBy;
    this.code = code;
    this.language = language;
    this.input = input;
  }

  public Code(String createdBy, SaveCodeRequest request) {
    this.createdBy = createdBy;
    this.id = request.getId();
    this.title = request.getTitle();
    this.code = request.getCode();
    this.language = request.getLanguage();
    this.input = request.getInput();
  }
}
