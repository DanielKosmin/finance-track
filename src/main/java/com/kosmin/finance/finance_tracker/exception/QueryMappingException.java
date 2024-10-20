package com.kosmin.finance.finance_tracker.exception;

public class QueryMappingException extends RuntimeException {
  public QueryMappingException(
      String message, String exceptionType, String exceptionMessage, String table) {
    super(
        String.format(
            "%s :: Exception Type:%s, Exception Message:%s :: For Table: %s",
            message, exceptionType, exceptionMessage, table));
  }
}
