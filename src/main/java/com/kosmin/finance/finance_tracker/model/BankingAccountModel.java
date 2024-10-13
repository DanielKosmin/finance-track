package com.kosmin.finance.finance_tracker.model;

import com.opencsv.bean.CsvBindByName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankingAccountModel {

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

  public void setTransactionDate(String transactionDate) {
    this.transactionDate = transactionDate;
    formatDate();
  }

  public void formatDate() {
    if (this.getTransactionDate() != null) {
      this.transactionDate = formattedDate(this.getTransactionDate());
    }
  }

  private String formattedDate(String transactionDate) {
    SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yy");
    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      return outputFormat.format(inputFormat.parse(transactionDate));
    } catch (ParseException e) {
      return null;
    }
  }
}
