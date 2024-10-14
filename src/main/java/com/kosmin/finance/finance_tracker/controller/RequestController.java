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

  @PostMapping("create")
  public ResponseEntity<Response> createTables() {
    return financeTrackerService.createTables();
  }

  @PostMapping("insert")
  public ResponseEntity<String> insertRecords(@RequestParam("file") MultipartFile file) {

    return financeTrackerService.insertRecords(file);
  }

  @PutMapping("map_relationship")
  public ResponseEntity<Response> mapRelationship(
      @Valid @RequestBody TransactionMappingRequest request) {
    return financeTrackerService.createTableRelationship(request);
  }

  @GetMapping("records")
  public ResponseEntity<Response> getAllFinancialRecords() {

    return financeTrackerService.getAllFinancialRecords();
  }
}
