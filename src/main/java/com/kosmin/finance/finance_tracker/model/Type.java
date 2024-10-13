package com.kosmin.finance.finance_tracker.model;

import lombok.Getter;

@Getter
public enum Type {
  BANKING("Banking"),
  CREDIT("Credit");

  private final String value;

  Type(String value) {
    this.value = value;
  }
}
