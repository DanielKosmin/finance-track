package com.kosmin.finance.finance_tracker.exception;

import java.time.LocalDate;

public class InvalidQueryParamaterComboException extends RuntimeException {
  public InvalidQueryParamaterComboException(
      String message,
      Boolean bankingTable,
      Boolean creditTable,
      LocalDate startDate,
      LocalDate endDate) {
    super(
        String.format(
            "%s: Start Date: %s, End Date: %s, Banking Table: %s, Credit Table: %s",
            message, startDate, endDate, bankingTable, creditTable));
  }
}
