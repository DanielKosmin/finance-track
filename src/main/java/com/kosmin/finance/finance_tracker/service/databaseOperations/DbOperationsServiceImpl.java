package com.kosmin.finance.finance_tracker.service.databaseOperations;

import com.kosmin.finance.finance_tracker.domain.SqlRepository;
import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
import com.kosmin.finance.finance_tracker.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbOperationsServiceImpl implements DbOperationsService {

  private final SqlRepository sqlRepository;

  @Override
  public void insertBankingInformation(BankingAccountModel bankingAccountModel) {
    sqlRepository.insertBankingInformation(bankingAccountModel);
  }

  @Override
  public void insertCreditInformation(CreditCardRecordsModel creditCardRecordsModel) {
    sqlRepository.insertCreditInformation(creditCardRecordsModel);
  }

  @Override
  public Response getAllFinancialRecords() {
    return sqlRepository.getAllFinancialRecords();
  }
}
