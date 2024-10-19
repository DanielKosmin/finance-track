package com.kosmin.finance.finance_tracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionMappingRequest {

  @NotBlank(message = "Start Date cannot be empty and must be in the format YYYY-MM-DD")
  @Pattern(
      regexp = "^\\d{4}-\\d{2}-\\d{2}$",
      message = "Start Date must be in the format YYYY-MM-DD")
  private String transactionStartDate;

  @NotBlank(message = "End Date cannot be empty and must be in the format YYYY-MM-DD")
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "End Date must be in the format YYYY-MM-DD")
  private String transactionEndDate;

  @NotBlank(message = "Transaction Description must contain keywords for table search")
  @Pattern(regexp = "^%.*%$", message = "Transaction Description must be wrapped in '%'")
  private String transactionDescription;

  @NotBlank(message = "Transaction Type must not be empty")
  private String transactionType;
}
