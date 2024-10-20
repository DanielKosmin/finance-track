package com.kosmin.finance.finance_tracker.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmin.finance.finance_tracker.domain.create.CreateTables;
import com.kosmin.finance.finance_tracker.domain.insert.InsertTableRecords;
import com.kosmin.finance.finance_tracker.domain.update.UpdateForeignKey;
import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
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

  protected final BankingAccountModel testBankingTableModel =
      BankingAccountModel.builder()
          .transactionDescription("chase")
          .transactionDate("2024-10-01")
          .transactionType("Debit")
          .transactionAmount(200.00)
          .balance(2000.00)
          .build();
  protected final CreditCardRecordsModel testCreditTableModel =
      CreditCardRecordsModel.builder()
          .transactionDate("2024-09-01")
          .transactionDescription("chase")
          .transactionCategory("transaction category")
          .transactionType("sale")
          .transactionAmount(200.00)
          .build();
  protected final TransactionMappingRequest transactionMappingRequest =
      TransactionMappingRequest.builder()
          .transactionStartDate("2024-09-01")
          .transactionEndDate("2024-09-30")
          .transactionDescription("%chase%")
          .transactionType("sale")
          .build();

  protected static final String COMMON_POST_URL = "expenses/v1/insert";
  protected static final String COMMON_GET_URL = "expenses/v1/records";
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
