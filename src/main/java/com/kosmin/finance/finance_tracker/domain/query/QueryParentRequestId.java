package com.kosmin.finance.finance_tracker.domain.query;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.exception.ParentTransactionNotFoundException;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import com.kosmin.finance.finance_tracker.util.FinanceTrackerUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class QueryParentRequestId {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;

  /**
   * this will query to find the parent id associated with a foreign key(s) table rows
   *
   * @param request query to update foreign key(s)
   * @return parent request ID
   */
  public int getParentRequestId(TransactionMappingRequest request) {
    final Map<String, Object> params = new HashMap<>();
    final List<String> updatedDates = FinanceTrackerUtil.incrementDates(request);
    params.put("startDate", updatedDates.get(0));
    params.put("endDate", updatedDates.get(1));
    params.put("description", request.getTransactionDescription());
    params.put("type", "Debit");
    log.info("Getting Primary Key with params {}", params);
    final Optional<Integer> results =
        namedParameterJdbcTemplate
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
