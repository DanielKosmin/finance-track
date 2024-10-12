package com.kosmin.finance.finance_tracker.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class FinancialRecordsEntity {

  private int recordId;
  private String accountNumber;
  private String transactionDescription;
  private String transactionDate;
  private String transactionType;
  private Double transactionAmount;
  private Double balance;
}
