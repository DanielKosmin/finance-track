package com.kosmin.finance.finance_tracker.service.databaseOperations;

import com.kosmin.finance.finance_tracker.domain.create.CreateTables;
import com.kosmin.finance.finance_tracker.domain.insert.InsertTableRecords;
import com.kosmin.finance.finance_tracker.domain.query.QueryForeignKeyRelationship;
import com.kosmin.finance.finance_tracker.domain.query.QueryTableRecords;
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

  private final InsertTableRecords insertTableRecords;
  private final UpdateForeignKey updateForeignKey;
  private final QueryTableRecords queryTableRecords;
  private final CreateTables createTables;
  private final QueryForeignKeyRelationship queryForeignKeyRelationship;

  @Override
  public void createTables() {
    createTables.createTables();
  }

  @Override
  public void insertBankingInformation(BankingAccountModel bankingAccountModel) {
    insertTableRecords.insertBankingInformation(bankingAccountModel);
  }

  @Override
  public void insertCreditInformation(CreditCardRecordsModel creditCardRecordsModel) {
    insertTableRecords.insertCreditInformation(creditCardRecordsModel);
  }

  @Override
  public Response getTableRecords(boolean bankingTable, boolean creditTable) {
    return queryTableRecords.getTableRecords(bankingTable, creditTable);
  }

  @Override
  public Response createTableRelationship(TransactionMappingRequest transactionMappingRequest) {
    return updateForeignKey.createTableRelationship(transactionMappingRequest);
  }

  @Override
  public Response getForeignKeyRelationship(String startDate, String endDate) {
    return queryForeignKeyRelationship.getForeignKeyRelationship(startDate, endDate);
  }
}
