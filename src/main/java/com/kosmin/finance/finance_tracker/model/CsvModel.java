package com.kosmin.finance.finance_tracker.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class CsvModel {
  @CsvBindByName(column = "Account Number")
  private String accountNumber;

  @CsvBindByName(column = "Transaction Description")
  private String transactionDescription;

  @CsvBindByName(column = "Transaction Date")
  private String transactionDate;

  @CsvBindByName(column = "Transaction Type")
  private String transactionType;

  @CsvBindByName(column = "Transaction Amount")
  private Double transactionAmount;

  @CsvBindByName(column = "Balance")
  private Double balance;
}
