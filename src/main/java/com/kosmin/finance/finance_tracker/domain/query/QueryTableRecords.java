package com.kosmin.finance.finance_tracker.domain.query;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.model.FinancialRecordsEntity;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import java.sql.ResultSet;
import java.sql.SQLException;
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

  public Response getBankingTable() {
    final List<FinancialRecordsEntity> entities =
        jdbcTemplate.query(sqlQueriesConfig.getMap().get("get-banking-record"), this::mapRows);
    if (entities.isEmpty()) throw new RuntimeException("No banking Records found");
    return Response.builder()
        .status(Status.SUCCESS.getValue())
        .numberOfRecords(entities.size())
        .records(entities)
        .build();
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
