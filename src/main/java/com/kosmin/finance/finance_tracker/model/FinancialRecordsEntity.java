package com.kosmin.finance.finance_tracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordsEntity {

  private int recordId;
  private int parentId;
  private String transactionDescription;
  private String transactionDate;
  private String transactionType;
  private String transactionCategory;
  private Double transactionAmount;
  private Double balance;
}
