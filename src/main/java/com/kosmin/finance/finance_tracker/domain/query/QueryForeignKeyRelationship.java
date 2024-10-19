package com.kosmin.finance.finance_tracker.domain.query;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.exception.ForeignKeyRelationshipNotFoundException;
import com.kosmin.finance.finance_tracker.model.ForeignKeyEntity;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class QueryForeignKeyRelationship {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;

  public Response getForeignKeyRelationship(String startDate, String endDate) {
    Map<String, Object> params = new HashMap<>();
    params.put("startDate", startDate);
    params.put("endDate", endDate);
    List<ForeignKeyEntity> entities =
        namedParameterJdbcTemplate.query(
            sqlQueriesConfig.getMap().get("get-foreign-key-relationship"), params, this::mapRows);
    if (entities.isEmpty())
      throw new ForeignKeyRelationshipNotFoundException("No foreign key relationship found");
    return Response.builder()
        .status(Status.SUCCESS.getValue())
        .numberOfRecords(entities.size())
        .foreignKeyEntities(entities)
        .build();
  }

  private ForeignKeyEntity mapRows(ResultSet rs, int rowNum) throws SQLException {
    return ForeignKeyEntity.builder()
        .creditTransactionDate(rs.getString("credit_transaction_date"))
        .creditTransactionDescription(rs.getString("credit_transaction_description"))
        .creditTransactionCategory(rs.getString("credit_transaction_category"))
        .creditTransactionAmount(rs.getDouble("credit_transaction_amount"))
        .checkingTransactionAmount(rs.getDouble(("checking_transaction_amount")))
        .checkingTransactionDate(rs.getString("checking_transaction_date"))
        .build();
  }
}
