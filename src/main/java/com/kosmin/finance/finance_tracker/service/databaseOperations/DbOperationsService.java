package com.kosmin.finance.finance_tracker.service.databaseOperations;

import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
import com.kosmin.finance.finance_tracker.model.Response;

public interface DbOperationsService {

  void insertBankingInformation(BankingAccountModel bankingAccountModel);

  void insertCreditInformation(CreditCardRecordsModel creditCardRecordsModel);

  Response getAllFinancialRecords();
}
