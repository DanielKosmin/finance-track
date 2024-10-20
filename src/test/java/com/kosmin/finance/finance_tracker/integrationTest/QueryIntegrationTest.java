package com.kosmin.finance.finance_tracker.integrationTest;

import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
import com.kosmin.finance.finance_tracker.model.FinancialRecordsEntity;
import com.kosmin.finance.finance_tracker.model.ForeignKeyEntity;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class QueryIntegrationTest extends BaseIntegrationTest {

  private final BankingAccountModel testBankingAccountModel =
      BankingAccountModel.builder()
          .transactionDescription("chase")
          .transactionDate("2024-10-01")
          .transactionType("Debit")
          .transactionAmount(200.00)
          .balance(2000.00)
          .build();
  private final CreditCardRecordsModel creditCardRecordsModel =
      CreditCardRecordsModel.builder()
          .transactionDate("2024-09-01")
          .transactionDescription("chase")
          .transactionCategory("transaction category")
          .transactionType("sale")
          .transactionAmount(200.00)
          .build();
  private final TransactionMappingRequest transactionMappingRequest =
      TransactionMappingRequest.builder()
          .transactionStartDate("2024-09-01")
          .transactionEndDate("2024-09-30")
          .transactionDescription("%chase%")
          .transactionType("sale")
          .build();

  @BeforeEach
  void setUp() {
    super.setUp();
    insertTableRecords.insertBankingInformation(testBankingAccountModel);
    insertTableRecords.insertCreditInformation(creditCardRecordsModel);
    updateForeignKey.createTableRelationship(transactionMappingRequest);
  }

  @Test
  @DisplayName("Get all checking table records")
  void testGetAllTableRecords() {
    var res = makeGetCall("/expenses/v1/banking", HttpStatus.OK);
    FinancialRecordsEntity financialRecordsEntity =
        Optional.ofNullable(res)
            .map(Response::getRecords)
            .flatMap(records -> records.stream().findFirst())
            .orElse(null);
    BankingAccountModel bankingAccountModel =
        objectMapper.convertValue(financialRecordsEntity, BankingAccountModel.class);
    // set incorrect date format since class auto changes date format to the correct one
    bankingAccountModel.setTransactionDate("10/01/24");
    Assertions.assertEquals(testBankingAccountModel, bankingAccountModel);
  }

  @Test
  @DisplayName("get all foreign key relationship results, only Inner Join, no results found")
  void testGetForeignKeyRelationshipNoResults() {
    String startDate = "2024-01-01";
    String endDate = "2024-01-31";
    Response res =
        makeGetCall(
            String.format("/expenses/v1?startDate=%s&endDate=%s", startDate, endDate),
            HttpStatus.NOT_FOUND);
    Assertions.assertNotNull(res);
    Assertions.assertEquals(
        res.getErrorMessage(),
        String.format(
            "No foreign key relationship found for date range: Start Date: %s, End Date: %s",
            startDate, endDate));
  }

  @Test
  @DisplayName("get all foreign key relationship results, test when incorrect params are given")
  void testGetForeignKeyRelationshipIncorrectParams() {
    String startDate = "2024-01";
    String endDate = "2024-01";
    Response res =
        makeGetCall(
            String.format("/expenses/v1?startDate=%s&endDate=%s", startDate, endDate),
            HttpStatus.BAD_REQUEST);
    Assertions.assertNotNull(res);
    Assertions.assertEquals(res.getMessage(), "use the correct date format: yyyy-MM-dd");
    Assertions.assertNotNull(res.getErrorMessage());
  }

  @Test
  @DisplayName("get all foreign key relationship results, test when no query params are given")
  void testGetForeignKeyRelationshipMissingParams() {
    Response res = makeGetCall("/expenses/v1", HttpStatus.BAD_REQUEST);
    Assertions.assertNotNull(res);
    Assertions.assertTrue(res.getErrorMessage().contains("startDate"));
  }

  @Test
  @DisplayName("get all foreign key relationship results, valid response given")
  void testGetForeignKeyRelationship() {
    String startDate = "2024-09-01";
    String endDate = "2024-09-30";
    Response res =
        makeGetCall(
            String.format("/expenses/v1?startDate=%s&endDate=%s", startDate, endDate),
            HttpStatus.OK);
    Assertions.assertNotNull(res);
    Assertions.assertEquals(
        res.getForeignKeyEntities(),
        List.of(
            ForeignKeyEntity.builder()
                .creditTransactionDate(creditCardRecordsModel.getTransactionDate())
                .creditTransactionDescription(creditCardRecordsModel.getTransactionDescription())
                .creditTransactionCategory(creditCardRecordsModel.getTransactionCategory())
                .creditTransactionAmount(creditCardRecordsModel.getTransactionAmount())
                .checkingTransactionAmount(testBankingAccountModel.getTransactionAmount())
                .checkingTransactionDate(testBankingAccountModel.getTransactionDate())
                .build()));
  }

  private Response makeGetCall(String uri, HttpStatus status) {
    return webTestClient
        .get()
        .uri(uri)
        .exchange()
        .expectStatus()
        .isEqualTo(status)
        .expectBody(Response.class)
        .returnResult()
        .getResponseBody();
  }
}
