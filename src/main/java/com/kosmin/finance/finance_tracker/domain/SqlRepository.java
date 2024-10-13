package com.kosmin.finance.finance_tracker.domain;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.model.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SqlRepository {

  private final JdbcTemplate jdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;

  public void insertBankingInformation(BankingAccountModel bankingAccountModel) {
    int insertionResponse =
        jdbcTemplate.update(
            sqlQueriesConfig.getMap().get("insert-banking-records"),
            bankingAccountModel.getTransactionDescription(),
            bankingAccountModel.getTransactionDate(),
            bankingAccountModel.getTransactionType(),
            bankingAccountModel.getTransactionAmount(),
            bankingAccountModel.getBalance());
    if (insertionResponse != 1)
      log.error("Insert banking Records Failed for {}", bankingAccountModel);
  }

  public void insertCreditInformation(CreditCardRecordsModel creditCardRecordsModel) {
    int insertionResponse =
        jdbcTemplate.update(
            sqlQueriesConfig.getMap().get("insert-credit-records"),
            creditCardRecordsModel.getTransactionDate(),
            creditCardRecordsModel.getTransactionDescription(),
            creditCardRecordsModel.getTransactionCategory(),
            creditCardRecordsModel.getTransactionType(),
            creditCardRecordsModel.getTransactionAmount());
    if (insertionResponse != 1)
      log.error("Insert credit Records Failed for {}", creditCardRecordsModel);
  }

  public Response getAllFinancialRecords() {
    List<FinancialRecordsEntity> entities =
        jdbcTemplate.query(sqlQueriesConfig.getMap().get("get-banking-record"), this::mapRows);
    if (entities.isEmpty()) throw new RuntimeException("No banking Records found");
    return Response.builder().status(Status.SUCCESS.getValue()).records(entities).build();
  }

  private FinancialRecordsEntity mapRows(ResultSet rs, int rowNum) throws SQLException {
    return FinancialRecordsEntity.builder()
        .recordId(rs.getInt("record_id"))
        .transactionDescription(rs.getString("transaction_description"))
        .transactionDate(rs.getString("transaction_date"))
        .transactionType(rs.getString("transaction_type"))
        .transactionAmount(rs.getDouble(("transaction_amount")))
        .balance(rs.getDouble("balance"))
        .build();
  }
}
