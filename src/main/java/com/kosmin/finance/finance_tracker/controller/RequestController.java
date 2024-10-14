package com.kosmin.finance.finance_tracker.controller;

import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import com.kosmin.finance.finance_tracker.service.FinanceTrackerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("expenses/v1")
@RequiredArgsConstructor
@Validated
public class RequestController {

  private final FinanceTrackerService financeTrackerService;

  @PostMapping("upload")
  public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {

    return financeTrackerService.processCsv(file);
  }

  @PostMapping("map_relationship")
  public ResponseEntity<Response> mapRelationship(
      @Valid @RequestBody TransactionMappingRequest request) {
    return financeTrackerService.createTableRelationship(request);
  }

  @GetMapping("records")
  public ResponseEntity<Response> getAllFinancialRecords() {

    return financeTrackerService.getAllFinancialRecords();
  }
}
