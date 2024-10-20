package com.kosmin.finance.finance_tracker.integrationTest;

import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
import com.kosmin.finance.finance_tracker.model.FinancialRecordsEntity;
import com.kosmin.finance.finance_tracker.model.ForeignKeyEntity;
import com.kosmin.finance.finance_tracker.model.Response;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.HttpStatus;

public class QueryIntegrationTest extends BaseIntegrationTest {

  @BeforeEach
  void setUp() {
    super.setUp();
    insertTableRecords.insertBankingInformation(testBankingTableModel);
    insertTableRecords.insertCreditInformation(testCreditTableModel);
    updateForeignKey.createTableRelationship(transactionMappingRequest);
  }

  @Test
  @DisplayName("Get all checking table records")
  void testGetAllBankingRecords() {
    final var res = makeGetCall(COMMON_GET_URL + "?bankingTable=true", HttpStatus.OK);
    final FinancialRecordsEntity financialRecordsEntity =
        Optional.ofNullable(res)
            .map(Response::getRecords)
            .flatMap(records -> records.stream().findFirst())
            .orElse(null);
    final BankingAccountModel bankingAccountModel =
        objectMapper.convertValue(financialRecordsEntity, BankingAccountModel.class);
    // set incorrect date format since class auto changes date format to the correct one
    bankingAccountModel.setTransactionDate("10/01/24");
    Assertions.assertEquals(testBankingTableModel, bankingAccountModel);
  }

  @Test
  @DisplayName("Get all credit table records")
  void testGetAllCreditRecords() {
    final var res = makeGetCall(COMMON_GET_URL + "?creditTable=true", HttpStatus.OK);
    final FinancialRecordsEntity financialRecordsEntity =
        Optional.ofNullable(res)
            .map(Response::getRecords)
            .flatMap(records -> records.stream().findFirst())
            .orElse(null);
    final CreditCardRecordsModel creditCardRecordsModel =
        objectMapper.convertValue(financialRecordsEntity, CreditCardRecordsModel.class);
    // set incorrect date format since class auto changes date format to the correct one
    creditCardRecordsModel.setTransactionDate("09/01/2024");
    Assertions.assertEquals(testCreditTableModel, creditCardRecordsModel);
  }

  @Test
  @DisplayName("Get all table records")
  void testGetAllTableRecords() {
    final var res =
        makeGetCall(COMMON_GET_URL + "?creditTable=true&bankingTable=true", HttpStatus.OK);
    final List<FinancialRecordsEntity> financialRecordsEntity =
        Optional.ofNullable(res).map(Response::getRecords).orElse(null);
    Optional.ofNullable(financialRecordsEntity).stream()
        .flatMap(Collection::stream)
        .forEach(
            records -> {
              if (StringUtils.isNotBlank(records.getTransactionCategory())) {
                final CreditCardRecordsModel creditCardRecordsModel =
                    objectMapper.convertValue(records, CreditCardRecordsModel.class);
                creditCardRecordsModel.setTransactionDate("09/01/2024");
                Assertions.assertEquals(testCreditTableModel, creditCardRecordsModel);
              } else {
                final BankingAccountModel bankingAccountModel =
                    objectMapper.convertValue(records, BankingAccountModel.class);
                bankingAccountModel.setTransactionDate("10/01/24");
                Assertions.assertEquals(testBankingTableModel, bankingAccountModel);
              }
            });
  }

  @Test
  @DisplayName("get all foreign key relationship results, only Inner Join, no results found")
  void testGetForeignKeyRelationshipNoResults() {
    final String startDate = "2024-01-01";
    final String endDate = "2024-01-31";
    final Response res =
        makeGetCall(
            String.format(COMMON_GET_URL + "?startDate=%s&endDate=%s", startDate, endDate),
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
    final String startDate = "2024-01";
    final String endDate = "2024-01";
    final Response res =
        makeGetCall(
            String.format(COMMON_GET_URL + "?startDate=%s&endDate=%s", startDate, endDate),
            HttpStatus.BAD_REQUEST);
    Assertions.assertNotNull(res);
    Assertions.assertEquals(res.getMessage(), "use the correct date format: yyyy-MM-dd");
    Assertions.assertNotNull(res.getErrorMessage());
  }

  @Test
  @DisplayName("get records, no query params given")
  void testGetForeignKeyRelationshipMissingParams() {
    final Response res = makeGetCall(COMMON_GET_URL, HttpStatus.BAD_REQUEST);
    Assertions.assertNotNull(res);
    Assertions.assertEquals(
        res.getErrorMessage(),
        "Invalid Query Param Combo: Start Date: null, End Date: null, Banking Table: null, Credit Table: null");
  }

  @Test
  @DisplayName("get all foreign key relationship results, valid response given")
  void testGetForeignKeyRelationship() {
    final String startDate = "2024-09-01";
    final String endDate = "2024-09-30";
    final Response res =
        makeGetCall(
            String.format(COMMON_GET_URL + "?startDate=%s&endDate=%s", startDate, endDate),
            HttpStatus.OK);
    Assertions.assertNotNull(res);
    Assertions.assertEquals(
        res.getForeignKeyEntities(),
        List.of(
            ForeignKeyEntity.builder()
                .creditTransactionDate(testCreditTableModel.getTransactionDate())
                .creditTransactionDescription(testCreditTableModel.getTransactionDescription())
                .creditTransactionCategory(testCreditTableModel.getTransactionCategory())
                .creditTransactionAmount(testCreditTableModel.getTransactionAmount())
                .checkingTransactionAmount(testBankingTableModel.getTransactionAmount())
                .checkingTransactionDate(testBankingTableModel.getTransactionDate())
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
