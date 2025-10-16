package com.surest.exception;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Handle IllegalArgumentException
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
    ErrorResponse error =
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  // Handle EntityNotFoundException
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
    ErrorResponse error =
        new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  // Optional: Fallback for any other exception
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    ErrorResponse error =
        new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Something went wrong, please try again later.",
            LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
