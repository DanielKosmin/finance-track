package com.kosmin.finance.finance_tracker.exception;

public class ForeignKeyRelationshipNotFoundException extends RuntimeException {
  public ForeignKeyRelationshipNotFoundException(String message) {
    super(message);
  }
}
