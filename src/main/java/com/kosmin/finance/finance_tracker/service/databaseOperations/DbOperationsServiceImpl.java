package com.kosmin.finance.finance_tracker.service.databaseOperations;

import com.kosmin.finance.finance_tracker.domain.SqlRepository;
import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbOperationsServiceImpl implements DbOperationsService {

  private final SqlRepository sqlRepository;

  @Override
  public void insertFinancialRecords(BankingAccountModel bankingAccountModel) {
    sqlRepository.insertFinancialRecords(bankingAccountModel);
  }

  @Override
  public Response getAllFinancialRecords() {
    return sqlRepository.getAllFinancialRecords();
  }
}
