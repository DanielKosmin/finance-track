package com.kosmin.finance.finance_tracker.service.databaseOperations;

import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.Response;

public interface DbOperationsService {

  void insertFinancialRecords(BankingAccountModel bankingAccountModel);

  Response getAllFinancialRecords();
}
