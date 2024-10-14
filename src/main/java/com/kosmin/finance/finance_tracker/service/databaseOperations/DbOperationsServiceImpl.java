package com.kosmin.finance.finance_tracker.service.databaseOperations;

import com.kosmin.finance.finance_tracker.domain.SqlRepository;
import com.kosmin.finance.finance_tracker.domain.update.UpdateForeignKey;
import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbOperationsServiceImpl implements DbOperationsService {

  private final SqlRepository sqlRepository;
  private final UpdateForeignKey updateForeignKey;

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

  @Override
  public Response createTableRelationship(TransactionMappingRequest transactionMappingRequest) {
    return updateForeignKey.createTableRelationship(transactionMappingRequest);
  }
}
