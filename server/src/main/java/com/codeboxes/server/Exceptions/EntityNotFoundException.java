package com.codeboxes.server.Exceptions;

public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException() {
    super("Entity not found");
  }

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public EntityNotFoundException(Throwable cause) {
    super(cause);
  }
}
