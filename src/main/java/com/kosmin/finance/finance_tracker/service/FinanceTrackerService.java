package com.kosmin.finance.finance_tracker.service;

import com.kosmin.finance.finance_tracker.exception.ParentTransactionNotFoundException;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import com.kosmin.finance.finance_tracker.model.Type;
import com.kosmin.finance.finance_tracker.service.databaseOperations.DbOperationsService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceTrackerService {

  private final AsyncCsvProcessingService asyncCsvProcessingService;
  private final DbOperationsService dbOperationsService;

  public ResponseEntity<Response> createTables() {
    try {
      dbOperationsService.createTables();
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(Response.builder().status(Status.SUCCESS.getValue()).build());
    } catch (BadSqlGrammarException e) {
      log.error(e.getMessage());
      return ResponseEntity.badRequest()
          .body(
              Response.builder()
                  .status(Status.FAILED.getValue())
                  .errorMessage("Unable to create tables, tables already exist")
                  .build());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.internalServerError()
          .body(
              Response.builder()
                  .status(Status.FAILED.getValue())
                  .errorMessage(e.getMessage())
                  .build());
    }
  }

  public ResponseEntity<String> insertRecords(MultipartFile file) {
    try {
      if (file.isEmpty()) {
        return ResponseEntity.badRequest().body("File is empty.");
      }

      String fileName = file.getOriginalFilename();
      if (fileName != null && !fileName.endsWith(".csv")) {
        return ResponseEntity.badRequest().body("File must have a .csv extension.");
      }

      asyncCsvProcessingService.handleCsvProcessing(
          file,
          (Optional.ofNullable(fileName)
                  .orElseThrow(() -> new RuntimeException("Filename is null"))
                  .contains("credit")
              ? Type.CREDIT
              : Type.BANKING));
      return ResponseEntity.accepted().body("CSV File Successfully received and processing");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  public ResponseEntity<Response> createTableRelationship(TransactionMappingRequest request) {
    Response response = Response.builder().build();
    try {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(dbOperationsService.createTableRelationship(request));
    } catch (EmptyResultDataAccessException | ParentTransactionNotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.badRequest()
          .body(
              response.toBuilder()
                  .errorMessage("no parent request id found for filtered search")
                  .build());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.internalServerError()
          .body(response.toBuilder().errorMessage(e.getMessage()).build());
    }
  }

  public ResponseEntity<Response> getAllFinancialRecords() {
    try {
      return ResponseEntity.ok().body(dbOperationsService.getAllFinancialRecords());
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(
              Response.builder()
                  .status(Status.FAILED.getValue())
                  .errorMessage(e.getMessage())
                  .build());
    }
  }
}
