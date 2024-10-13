package com.kosmin.finance.finance_tracker.model;

import com.opencsv.bean.CsvBindByName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditCardRecordsModel {

  @CsvBindByName(column = "Transaction Date")
  private String transactionDate;

  @CsvBindByName(column = "Description")
  private String transactionDescription;

  @CsvBindByName(column = "Category")
  private String transactionCategory;

  @CsvBindByName(column = "Type")
  private String transactionType;

  @CsvBindByName(column = "Amount")
  private Double transactionAmount;

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
    SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      return outputFormat.format(inputFormat.parse(transactionDate));
    } catch (ParseException e) {
      return null;
    }
  }
}
