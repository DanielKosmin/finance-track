package com.kosmin.finance.finance_tracker.domain.insert;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InsertTableRecords {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;

  public void insertBankingInformation(BankingAccountModel bankingAccountModel) {
    Map<String, Object> params = new HashMap<>();
    params.put("transactionDescription", bankingAccountModel.getTransactionDescription());
    params.put("transactionDate", bankingAccountModel.getTransactionDate());
    params.put("transactionType", bankingAccountModel.getTransactionType());
    params.put("transactionAmount", bankingAccountModel.getTransactionAmount());
    params.put("balance", bankingAccountModel.getBalance());

    int insertionResponse =
        jdbcTemplate.update(sqlQueriesConfig.getMap().get("insert-banking-records"), params);
    if (insertionResponse != 1) {
      log.error("Insert banking Records Failed for {}", bankingAccountModel);
    }
  }

  public void insertCreditInformation(CreditCardRecordsModel creditCardRecordsModel) {
    Map<String, Object> params = new HashMap<>();
    params.put("transactionDate", creditCardRecordsModel.getTransactionDate());
    params.put("transactionDescription", creditCardRecordsModel.getTransactionDescription());
    params.put("transactionCategory", creditCardRecordsModel.getTransactionCategory());
    params.put("transactionType", creditCardRecordsModel.getTransactionType());
    params.put("transactionAmount", creditCardRecordsModel.getTransactionAmount());
    int insertionResponse =
        jdbcTemplate.update(sqlQueriesConfig.getMap().get("insert-credit-records"), params);
    if (insertionResponse != 1)
      log.error("Insert credit Records Failed for {}", creditCardRecordsModel);
  }
}
