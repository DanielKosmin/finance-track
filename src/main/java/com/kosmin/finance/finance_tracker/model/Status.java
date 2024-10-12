package com.kosmin.finance.finance_tracker.model;

import lombok.Getter;

@Getter
public enum Status {
  SUCCESS("Success"),
  FAILED("Failed");

  private final String value;

  Status(String value) {
    this.value = value;
  }
}
