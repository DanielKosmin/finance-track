package com.kosmin.finance.finance_tracker.domain.update;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.domain.query.QueryParentRequestId;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UpdateForeignKey {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;
  private final QueryParentRequestId queryParentRequestId;

  public Response createTableRelationship(TransactionMappingRequest request) {
    final Map<String, Object> params = new HashMap<>();
    final int primaryKeyParentId = queryParentRequestId.getParentRequestId(request);
    params.put("startDate", request.getTransactionStartDate());
    params.put("endDate", request.getTransactionEndDate());
    params.put("parentId", primaryKeyParentId);
    params.put("type", request.getTransactionType());
    log.info("Updating foreign key(s) with params: {}", params);
    final int rowsAffected =
        jdbcTemplate.update(
            sqlQueriesConfig.getMap().get("update-foreign-key-with-parent-id"), params);
    return Response.builder()
        .status(Status.SUCCESS.getValue())
        .message(String.format("Updated Foreign Key value for %s rows", rowsAffected))
        .build();
  }
}
