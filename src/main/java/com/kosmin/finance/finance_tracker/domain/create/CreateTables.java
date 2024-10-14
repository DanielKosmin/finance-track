package com.kosmin.finance.finance_tracker.domain.create;

import com.kosmin.finance.finance_tracker.config.TableInsertConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CreateTables {

  private final JdbcTemplate jdbcTemplate;
  private final TableInsertConfig tableInsertConfig;

  public void createTables() {
    jdbcTemplate.execute(tableInsertConfig.getCreateBankingTable());
    jdbcTemplate.execute(tableInsertConfig.getCreateCreditTable());
  }
}
