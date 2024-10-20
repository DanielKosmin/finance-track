package com.kosmin.finance.finance_tracker.domain.query;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.exception.QueryMappingException;
import com.kosmin.finance.finance_tracker.model.FinancialRecordsEntity;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class QueryTableRecords {
  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;

  public Response getTableRecords(boolean bankingTable, boolean creditTable) {
    final List<FinancialRecordsEntity> entities = new ArrayList<>();
    if (bankingTable) {
      entities.addAll(
          jdbcTemplate.query(
              sqlQueriesConfig.getMap().get("get-banking-record"),
              (rs, rowNum) -> mapRows(rs, "bankingTable")));
    }
    if (creditTable) {
      entities.addAll(
          jdbcTemplate.query(
              sqlQueriesConfig.getMap().get("get-credit-record"),
              (rs, rowNum) -> mapRows(rs, "creditTable")));
    }
    if (entities.isEmpty()) throw new RuntimeException("No Records found");
    return Response.builder()
        .status(Status.SUCCESS.getValue())
        .numberOfRecords(entities.size())
        .records(entities)
        .build();
  }

  private FinancialRecordsEntity mapRows(ResultSet rs, String tableName) {
    try {
      switch (tableName) {
        case "bankingTable" -> {
          return FinancialRecordsEntity.builder()
              .recordId(rs.getInt("record_id"))
              .transactionDescription(rs.getString("transaction_description"))
              .transactionDate(rs.getString("transaction_date"))
              .transactionType(rs.getString("transaction_type"))
              .transactionAmount(rs.getDouble(("transaction_amount")))
              .balance(rs.getDouble("balance"))
              .build();
        }
        case "creditTable" -> {
          return FinancialRecordsEntity.builder()
              .recordId(rs.getInt("record_id"))
              .parentId(rs.getInt("parent_id"))
              .transactionDate(rs.getString("transaction_date"))
              .transactionDescription(rs.getString("transaction_description"))
              .transactionCategory(rs.getString("transaction_category"))
              .transactionType(rs.getString("transaction_type"))
              .transactionAmount(rs.getDouble("transaction_amount"))
              .build();
        }
        default ->
            throw new QueryMappingException("Default Case in switch statement", "", "", tableName);
      }
    } catch (Exception e) {
      throw new QueryMappingException(
          "Exception occurred mapping row results",
          e.getClass().getSimpleName(),
          e.getMessage(),
          tableName);
    }
  }
}
