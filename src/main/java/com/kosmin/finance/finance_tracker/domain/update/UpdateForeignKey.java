package com.kosmin.finance.finance_tracker.domain.update;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.exception.ParentTransactionNotFoundException;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

  public Response createTableRelationship(TransactionMappingRequest request) {
    Map<String, Object> params = new HashMap<>();
    int primaryKeyParentId = getParentRequestId(request, params);
    params.put("parentId", primaryKeyParentId);
    params.replace("type", "Sale");
    params.remove("description");
    log.info("Updating foreign key(s) with params: {}", params);
    int rowsAffected =
        jdbcTemplate.update(
            sqlQueriesConfig.getMap().get("update-foreign-key-with-parent-id"), params);
    return Response.builder()
        .status(Status.SUCCESS.getValue())
        .message(String.format("Updated Foreign Key value for %s rows", rowsAffected))
        .build();
  }

  private int getParentRequestId(TransactionMappingRequest request, Map<String, Object> params) {
    params.put("startDate", request.getTransactionStartDate());
    params.put("endDate", request.getTransactionEndDate());
    params.put("description", request.getTransactionDescription());
    params.put("type", request.getTransactionType());
    log.info("Getting Primary Key with params {}", params);
    Optional<Integer> results =
        jdbcTemplate
            .query(
                sqlQueriesConfig.getMap().get("get-parent-id-of-transaction"),
                params,
                (rs, rowNum) -> rs.getInt(1))
            .stream()
            .findFirst();
    return results.orElseThrow(
        () -> new ParentTransactionNotFoundException("No parent transaction found"));
  }
}
