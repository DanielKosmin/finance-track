package com.kosmin.finance.finance_tracker.controller;

import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import com.kosmin.finance.finance_tracker.service.FinanceTrackerService;
import jakarta.validation.Valid;
import java.io.IOException;
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
  public ResponseEntity<Response> insertRecords(@RequestParam("file") MultipartFile file)
      throws IOException {

    return financeTrackerService.insertRecords(file);
  }

  @PutMapping("map_relationship")
  public ResponseEntity<Response> mapRelationship(
      @Valid @RequestBody TransactionMappingRequest request) {
    return financeTrackerService.createTableRelationship(request);
  }

  @GetMapping("banking")
  public ResponseEntity<Response> getBankingTable() {

    return financeTrackerService.getBankingTable();
  }

  @GetMapping()
  public ResponseEntity<Response> getForeignKeyRelationship(
      @RequestParam String startDate, @RequestParam String endDate) {

    return financeTrackerService.getForeignKeyRelationship(startDate, endDate);
  }
}
