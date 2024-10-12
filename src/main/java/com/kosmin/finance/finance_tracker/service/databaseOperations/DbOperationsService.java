package com.kosmin.finance.finance_tracker.service.databaseOperations;

import com.kosmin.finance.finance_tracker.model.FinancialRecordsEntity;
import com.kosmin.finance.finance_tracker.model.Response;

public interface DbOperationsService {

  void insertFinancialRecords(FinancialRecordsEntity recordsEntity);

  Response getAllFinancialRecords();
}
