package com.kosmin.finance.finance_tracker.domain.update;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.exception.ParentTransactionNotFoundException;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class UpdateForeignKey {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final SqlQueriesConfig sqlQueriesConfig;

  public Response createTableRelationship(TransactionMappingRequest request) {
    Map<String, Object> params = new HashMap<>();
    int primaryKeyParentId = getParentRequestId(request);
    params.put("startDate", request.getTransactionStartDate());
    params.put("endDate", request.getTransactionEndDate());
    params.put("parentId", primaryKeyParentId);
    params.put("type", request.getTransactionType());
    log.info("Updating foreign key(s) with params: {}", params);
    int rowsAffected =
        jdbcTemplate.update(
            sqlQueriesConfig.getMap().get("update-foreign-key-with-parent-id"), params);
    return Response.builder()
        .status(Status.SUCCESS.getValue())
        .message(String.format("Updated Foreign Key value for %s rows", rowsAffected))
        .build();
  }

  private int getParentRequestId(TransactionMappingRequest request) {
    Map<String, Object> params = new HashMap<>();
    List<String> updatedDates = getParentTransactionDates(request);
    params.put("startDate", updatedDates.get(0));
    params.put("endDate", updatedDates.get(1));
    params.put("description", request.getTransactionDescription());
    params.put("type", "Debit");
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

  /**
   * Method increments the start and end date of the foreign key relationship by one since the
   * withdrawals are made at the beginning of the month meaning all transactions are in the month
   * prior. ex. All september transactions will correlate to a withdrawal at the beginning of
   * October.
   *
   * @param request foreign key relationship user wishes to relate a banking transaction to credit
   *     card transactions associated with a withdrawal
   * @return list of strings. First string will have the start date incremented by one and the
   *     second string will have the end date incremented by one month
   */
  private List<String> getParentTransactionDates(TransactionMappingRequest request) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    LocalDate startDate = LocalDate.parse(request.getTransactionStartDate(), formatter);
    LocalDate endDate = LocalDate.parse(request.getTransactionEndDate(), formatter);

    LocalDate updatedStartDate = startDate.plusMonths(1);
    LocalDate updatedEndDate = endDate.plusMonths(1);

    return List.of(updatedStartDate.format(formatter), updatedEndDate.format(formatter));
  }
}
