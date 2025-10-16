package com.surest.exception;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ErrorResponse {
  private int status;
  private String message;
  private LocalDateTime timestamp;

  public ErrorResponse(int status, String message, LocalDateTime timestamp) {
    this.status = status;
    this.message = message;
    this.timestamp = timestamp;
  }

  // Getters and Setters (or use Lombok @Data)
}
