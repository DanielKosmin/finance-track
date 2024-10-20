package com.kosmin.finance.finance_tracker.util;

import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FinanceTrackerUtil {

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
  public static List<String> incrementDates(Object request) {
    if (request instanceof TransactionMappingRequest) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      LocalDate startDate =
          LocalDate.parse(
              ((TransactionMappingRequest) request).getTransactionStartDate(), formatter);
      LocalDate endDate =
          LocalDate.parse(((TransactionMappingRequest) request).getTransactionEndDate(), formatter);

      LocalDate updatedStartDate = startDate.plusMonths(1);
      LocalDate updatedEndDate = endDate.plusMonths(1);

      return List.of(updatedStartDate.format(formatter), updatedEndDate.format(formatter));
    }
    return List.of();
  }
}
