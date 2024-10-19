package com.kosmin.finance.finance_tracker.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmin.finance.finance_tracker.domain.create.CreateTables;
import com.kosmin.finance.finance_tracker.domain.insert.InsertTableRecords;
import com.kosmin.finance.finance_tracker.domain.update.UpdateForeignKey;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("int")
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

  @Autowired protected CreateTables createTables;
  @Autowired protected JdbcTemplate jdbcTemplate;
  @Autowired protected InsertTableRecords insertTableRecords;
  @Autowired protected UpdateForeignKey updateForeignKey;
  @Autowired protected ObjectMapper objectMapper;
  @Autowired protected WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    createTables.createTables();
  }

  @AfterEach
  void tearDown() {
    jdbcTemplate.execute("ALTER TABLE credit_records DROP FOREIGN KEY credit_records_ibfk_1");
    jdbcTemplate.execute("DROP TABLE IF EXISTS checking_account_records");
    jdbcTemplate.execute("DROP TABLE IF EXISTS credit_records");
  }
}
