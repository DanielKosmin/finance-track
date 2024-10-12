package com.kosmin.finance.finance_tracker.service;

import com.kosmin.finance.finance_tracker.model.CsvModel;
import com.kosmin.finance.finance_tracker.model.FinancialRecordsEntity;
import com.kosmin.finance.finance_tracker.service.databaseOperations.DbOperationsService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncCsvProcessingService {
  private final DbOperationsService dbOperationsService;

  @Async
  public void handleCsvProcessing(MultipartFile file) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      CsvToBean<CsvModel> csvToBean =
          new CsvToBeanBuilder<CsvModel>(reader)
              .withType(CsvModel.class)
              .withIgnoreLeadingWhiteSpace(true)
              .build();
      List<CsvModel> csvModels = csvToBean.parse();

      csvModels.forEach(
          csvModel ->
              dbOperationsService.insertFinancialRecords(buildFinancialRecordsEntity(csvModel)));
    }
    log.info("Completed Insertions into Financial Records Table");
  }

  private FinancialRecordsEntity buildFinancialRecordsEntity(CsvModel csvModel) {
    SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yy");
    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = "";
    try {
      Date date = inputFormat.parse(csvModel.getTransactionDate());
      formattedDate = outputFormat.format(date);
    } catch (ParseException e) {
      log.error(e.getMessage());
    }
    return FinancialRecordsEntity.builder()
        .accountNumber(csvModel.getAccountNumber())
        .transactionDescription(csvModel.getTransactionDescription())
        .transactionDate(formattedDate)
        .transactionType(csvModel.getTransactionType())
        .transactionAmount(csvModel.getTransactionAmount())
        .balance(csvModel.getBalance())
        .build();
  }
}
