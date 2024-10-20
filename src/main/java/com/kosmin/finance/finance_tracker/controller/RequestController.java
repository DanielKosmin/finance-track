package com.kosmin.finance.finance_tracker.controller;

import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import com.kosmin.finance.finance_tracker.service.FinanceTrackerService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

  @GetMapping("records")
  public ResponseEntity<Response> getTableRecords(
      @RequestParam(required = false) Boolean bankingTable,
      @RequestParam(required = false) Boolean creditTable,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

    return financeTrackerService.getTableRecords(bankingTable, creditTable, startDate, endDate);
  }
}
