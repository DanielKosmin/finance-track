package com.kosmin.finance.finance_tracker.exception;

import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GetForeignKeyExceptionHandler {
  /**
   * Handle invalid date format such as yyyy-MM being sent instead of yyyy-MM-dd
   *
   * @param ex exception thrown during invalid date format
   * @return response entity with error message
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Response> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    return ResponseEntity.badRequest()
        .body(
            Response.builder()
                .status(Status.FAILED.getValue())
                .message("use the correct date format: yyyy-MM-dd")
                .errorMessage(ex.getMessage())
                .build());
  }

  // Handle missing query parameters
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Response> handleMissingParams(MissingServletRequestParameterException ex) {
    return ResponseEntity.badRequest()
        .body(
            Response.builder()
                .status(Status.FAILED.getValue())
                .errorMessage(ex.getMessage())
                .build());
  }
}
