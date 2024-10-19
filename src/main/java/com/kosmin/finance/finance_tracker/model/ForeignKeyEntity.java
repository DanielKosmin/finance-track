package com.kosmin.finance.finance_tracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyEntity {

  private String creditTransactionDate;
  private String creditTransactionDescription;
  private String creditTransactionCategory;
  private Double creditTransactionAmount;
  private Double checkingTransactionAmount;
  private String checkingTransactionDate;
}
