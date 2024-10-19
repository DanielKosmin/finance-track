package com.kosmin.finance.finance_tracker.exception;

public class ForeignKeyRelationshipNotFoundException extends RuntimeException {
  public ForeignKeyRelationshipNotFoundException(String message, String startDate, String endDate) {
    super(String.format("%s: Start Date: %s, End Date: %s", message, startDate, endDate));
  }
}
