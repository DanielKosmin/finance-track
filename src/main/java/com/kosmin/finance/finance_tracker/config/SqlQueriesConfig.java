package com.kosmin.finance.finance_tracker.config;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "queries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlQueriesConfig {

  private Map<String, String> map;
}
