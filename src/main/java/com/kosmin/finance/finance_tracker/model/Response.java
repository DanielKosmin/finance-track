package com.kosmin.finance.finance_tracker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
  private String status;
  private List<FinancialRecordsEntity> records;
  private String errorMessage;
}
