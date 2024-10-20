package com.kosmin.finance.finance_tracker.service;

import com.kosmin.finance.finance_tracker.exception.InvalidQueryParamaterComboException;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import com.kosmin.finance.finance_tracker.model.Type;
import com.kosmin.finance.finance_tracker.service.asyncService.AsyncCsvProcessingService;
import com.kosmin.finance.finance_tracker.service.databaseOperations.DbOperationsService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceTrackerService {

  private final AsyncCsvProcessingService asyncCsvProcessingService;
  private final DbOperationsService dbOperationsService;

  public ResponseEntity<Response> createTables() {
    dbOperationsService.createTables();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Response.builder().status(Status.SUCCESS.getValue()).build());
  }

  public ResponseEntity<Response> insertRecords(MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              Response.builder()
                  .status(Status.FAILED.getValue())
                  .errorMessage("File is empty.")
                  .build());
    }

    final String fileName = file.getOriginalFilename();
    if (fileName != null && !fileName.endsWith(".csv")) {
      return ResponseEntity.badRequest()
          .body(
              Response.builder()
                  .status(Status.FAILED.getValue())
                  .errorMessage("File must have a .csv extension.")
                  .build());
    }

    asyncCsvProcessingService.handleCsvProcessing(
        file,
        (Optional.ofNullable(fileName)
                .orElseThrow(() -> new RuntimeException("Filename is null"))
                .contains("credit")
            ? Type.CREDIT
            : Type.BANKING));
    return ResponseEntity.accepted()
        .body(
            Response.builder()
                .status(Status.SUCCESS.getValue())
                .message("CSV File Successfully received and processing")
                .build());
  }

  public ResponseEntity<Response> createTableRelationship(TransactionMappingRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(dbOperationsService.createTableRelationship(request));
  }

  public ResponseEntity<Response> getTableRecords(
      Boolean bankingTable, Boolean creditTable, LocalDate startDate, LocalDate endDate) {
    final boolean isBankingTable = Optional.ofNullable(bankingTable).orElse(false);
    final boolean isCreditTable = Optional.ofNullable(creditTable).orElse(false);
    if (isBankingTable || isCreditTable) {
      return ResponseEntity.ok()
          .body(dbOperationsService.getTableRecords(isBankingTable, isCreditTable));
    } else if (Optional.ofNullable(startDate).isPresent()
        && Optional.ofNullable(endDate).isPresent()) {
      return ResponseEntity.ok()
          .body(
              dbOperationsService.getForeignKeyRelationship(
                  startDate.toString(), endDate.toString()));
    } else {
      throw new InvalidQueryParamaterComboException(
          "Invalid Query Param Combo", bankingTable, creditTable, startDate, endDate);
    }
  }
}
