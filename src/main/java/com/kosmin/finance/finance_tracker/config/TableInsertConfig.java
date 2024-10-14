package com.kosmin.finance.finance_tracker.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tables")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInsertConfig {
  private String createBankingTable;
  private String createCreditTable;
}
