package com.kosmin.finance.finance_tracker.service.responseBuilder;

import com.kosmin.finance.finance_tracker.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseBuilderService {

  public ResponseEntity<Response> buildBadRequestResponse(String message, Response response) {
    return ResponseEntity.badRequest().body(response.toBuilder().errorMessage(message).build());
  }

  public ResponseEntity<Response> buildInternalErrorResponse(String message, Response response) {
    return ResponseEntity.internalServerError()
        .body(response.toBuilder().errorMessage(message).build());
  }

  public ResponseEntity<Response> buildNotFoundResponse(String message, Response response) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(response.toBuilder().errorMessage(message).build());
  }
}
