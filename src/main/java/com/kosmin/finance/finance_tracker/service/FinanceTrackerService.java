package com.kosmin.finance.finance_tracker.service;

import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import com.kosmin.finance.finance_tracker.service.databaseOperations.DbOperationsService;
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

  public ResponseEntity<String> processCsv(MultipartFile file) {
    try {
      if (file.isEmpty()) {
        return ResponseEntity.badRequest().body("File is empty.");
      }

      String fileName = file.getOriginalFilename();
      if (fileName != null && !fileName.endsWith(".csv")) {
        return ResponseEntity.badRequest().body("File must have a .csv extension.");
      }

      asyncCsvProcessingService.handleCsvProcessing(file);
      return ResponseEntity.accepted().body("CSV File Successfully received and processing");
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.internalServerError().body(e.getMessage());
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
