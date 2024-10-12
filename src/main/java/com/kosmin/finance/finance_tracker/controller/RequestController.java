package com.kosmin.finance.finance_tracker.controller;

import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.service.FinanceTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("expenses/v1")
@RequiredArgsConstructor
public class RequestController {

  private final FinanceTrackerService financeTrackerService;

  @PostMapping("upload")
  public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {

    return financeTrackerService.processCsv(file);
  }

  @GetMapping("records")
  public ResponseEntity<Response> getAllFinancialRecords() {

    return financeTrackerService.getAllFinancialRecords();
  }
}
