package com.kosmin.finance.finance_tracker.controller;

import com.kosmin.finance.finance_tracker.model.CsvModel;
import com.kosmin.finance.finance_tracker.service.CsvParsingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("expenses/v1")
@RequiredArgsConstructor
public class RequestController {

  private final CsvParsingService csvParsingService;

  @PostMapping("upload")
  public ResponseEntity<List<CsvModel>> uploadCsvFile(@RequestParam("file") MultipartFile file) {

    try {
      return ResponseEntity.ok(csvParsingService.processCsv(file));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
