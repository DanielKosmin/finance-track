package com.kosmin.finance.finance_tracker.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmin.finance.finance_tracker.domain.create.CreateTables;
import com.kosmin.finance.finance_tracker.domain.insert.InsertTableRecords;
import com.kosmin.finance.finance_tracker.domain.update.UpdateForeignKey;
import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
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

  @Autowired private CreateTables createTables;
  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private InsertTableRecords insertTableRecords;
  @Autowired private UpdateForeignKey updateForeignKey;
  @Autowired protected ObjectMapper objectMapper;
  @Autowired protected WebTestClient webTestClient;
  protected BankingAccountModel testBankingAccountModel =
      BankingAccountModel.builder()
          .transactionDescription("chase")
          .transactionDate("2024-10-01")
          .transactionType("Debit")
          .transactionAmount(200.00)
          .balance(2000.00)
          .build();
  protected CreditCardRecordsModel creditCardRecordsModel =
      CreditCardRecordsModel.builder()
          .transactionDate("2024-09-01")
          .transactionDescription("chase")
          .transactionCategory("transaction category")
          .transactionType("sale")
          .transactionAmount(200.00)
          .build();
  protected TransactionMappingRequest transactionMappingRequest =
      TransactionMappingRequest.builder()
          .transactionStartDate("2024-09-01")
          .transactionEndDate("2024-09-30")
          .transactionDescription("%chase%")
          .transactionType("sale")
          .build();

  @BeforeEach
  void setUp() {
    createTables.createTables();
    insertTableRecords.insertBankingInformation(testBankingAccountModel);
    insertTableRecords.insertCreditInformation(creditCardRecordsModel);
    updateForeignKey.createTableRelationship(transactionMappingRequest);
  }

  @AfterEach
  void tearDown() {
    jdbcTemplate.execute("ALTER TABLE credit_records DROP FOREIGN KEY credit_records_ibfk_1");
    jdbcTemplate.execute("DROP TABLE IF EXISTS checking_account_records");
    jdbcTemplate.execute("DROP TABLE IF EXISTS credit_records");
  }
}
