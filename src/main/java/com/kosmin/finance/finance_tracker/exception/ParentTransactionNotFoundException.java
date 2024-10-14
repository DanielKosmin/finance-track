package com.kosmin.finance.finance_tracker.exception;

public class ParentTransactionNotFoundException extends RuntimeException {
  public ParentTransactionNotFoundException(String message) {
    super(message);
  }
}
