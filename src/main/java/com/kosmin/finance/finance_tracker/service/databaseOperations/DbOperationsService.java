package com.kosmin.finance.finance_tracker.service.databaseOperations;

import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;

public interface DbOperationsService {

  void createTables();

  void insertBankingInformation(BankingAccountModel bankingAccountModel);

  void insertCreditInformation(CreditCardRecordsModel creditCardRecordsModel);

  Response getAllFinancialRecords();

  Response createTableRelationship(TransactionMappingRequest transactionMappingRequest);
}
