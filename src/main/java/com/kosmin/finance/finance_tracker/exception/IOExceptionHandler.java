package com.kosmin.finance.finance_tracker.exception;

import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IOExceptionHandler {
  @ExceptionHandler(value = IOException.class)
  public ResponseEntity<Response> handleIOException(IOException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            Response.builder()
                .status(Status.FAILED.getValue())
                .errorMessage("An error occurred while processing the file: " + ex.getMessage())
                .build());
  }
}
