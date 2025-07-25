package com.codeboxes.server.Configs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.codeboxes.server.DTOs.CommonResponse;

@ControllerAdvice
public class RestErrorHandler {
  @ExceptionHandler
  public ResponseEntity<CommonResponse<String>> handleAllExceptions(Exception e) {
    CommonResponse<String> response = new CommonResponse<>(e.getLocalizedMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(response);
  }
}
