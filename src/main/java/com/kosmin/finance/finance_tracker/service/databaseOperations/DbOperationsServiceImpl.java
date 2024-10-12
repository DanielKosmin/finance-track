package com.kosmin.finance.finance_tracker.service.databaseOperations;

import com.kosmin.finance.finance_tracker.domain.SqlRepository;
import com.kosmin.finance.finance_tracker.model.FinancialRecordsEntity;
import com.kosmin.finance.finance_tracker.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbOperationsServiceImpl implements DbOperationsService {

  private final SqlRepository sqlRepository;

  @Override
  public void insertFinancialRecords(FinancialRecordsEntity recordsEntity) {
    sqlRepository.insertFinancialRecords(recordsEntity);
  }

  @Override
  public Response getAllFinancialRecords() {
    return sqlRepository.getAllFinancialRecords();
  }
}
