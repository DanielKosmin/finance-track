package com.kosmin.finance.finance_tracker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
  private String status;
  private int numberOfRecords;
  private List<FinancialRecordsEntity> records;
  private List<ForeignKeyEntity> foreignKeyEntities;
  private String errorMessage;
  private String message;
}
