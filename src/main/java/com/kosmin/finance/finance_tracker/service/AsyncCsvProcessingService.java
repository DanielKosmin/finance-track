package com.kosmin.finance.finance_tracker.service;

import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
import com.kosmin.finance.finance_tracker.model.Type;
import com.kosmin.finance.finance_tracker.service.databaseOperations.DbOperationsService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
  public void handleCsvProcessing(MultipartFile file, Type type) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      switch (type) {
        case BANKING -> {
          CsvToBean<BankingAccountModel> csvToBean =
              new CsvToBeanBuilder<BankingAccountModel>(reader)
                  .withType(BankingAccountModel.class)
                  .withIgnoreLeadingWhiteSpace(true)
                  .build();
          List<BankingAccountModel> bankingAccountModels = csvToBean.parse();

          bankingAccountModels.forEach(dbOperationsService::insertBankingInformation);
        }
        case CREDIT -> {
          CsvToBean<CreditCardRecordsModel> csvToBean =
              new CsvToBeanBuilder<CreditCardRecordsModel>(reader)
                  .withType(CreditCardRecordsModel.class)
                  .withIgnoreLeadingWhiteSpace(true)
                  .build();
          List<CreditCardRecordsModel> bankingAccountModels = csvToBean.parse();

          bankingAccountModels.forEach(dbOperationsService::insertCreditInformation);
        }
      }
    }
    log.info("Completed Insertion(s) into {} Table", type);
  }
}
