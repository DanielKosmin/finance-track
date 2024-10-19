package com.kosmin.finance.finance_tracker.integrationTest;

import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

public class QueryIntegrationTest extends BaseIntegrationTest {

  @Test
  @DisplayName("Get all checking table records")
  void testGetAllTableRecords() {
    webTestClient
        .get()
        .uri("/expenses/v1/banking")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Response.class)
        .consumeWith(
            responseEntityExchangeResult ->
                Assertions.assertEquals(
                    Optional.ofNullable(responseEntityExchangeResult)
                        .map(EntityExchangeResult::getResponseBody)
                        .map(Response::getStatus)
                        .orElse(Status.FAILED.getValue()),
                    Status.SUCCESS.getValue()))
        .returnResult()
        .getResponseBody();
  }

  @Test
  @DisplayName("get all foreign key relationship results, only Inner Join, no results found")
  void testGetForeignKeyRelationshipNoResults() {
    String startDate = "2024-01-01";
    String endDate = "2024-01-31";
    Response res =
        webTestClient
            .get()
            .uri(String.format("/expenses/v1?startDate=%s&endDate=%s", startDate, endDate))
            .exchange()
            .expectStatus()
            .isNotFound()
            .expectBody(Response.class)
            .consumeWith(
                responseEntityExchangeResult ->
                    Assertions.assertEquals(
                        Optional.ofNullable(responseEntityExchangeResult)
                            .map(EntityExchangeResult::getResponseBody)
                            .map(Response::getStatus)
                            .orElse(Status.FAILED.getValue()),
                        Status.FAILED.getValue()))
            .returnResult()
            .getResponseBody();
    Assertions.assertEquals(
        Optional.ofNullable(res).map(Response::getErrorMessage).orElse("not found"),
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
        webTestClient
            .get()
            .uri(String.format("/expenses/v1?startDate=%s&endDate=%s", startDate, endDate))
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(Response.class)
            .consumeWith(
                responseEntityExchangeResult ->
                    Assertions.assertEquals(
                        Optional.ofNullable(responseEntityExchangeResult)
                            .map(EntityExchangeResult::getResponseBody)
                            .map(Response::getStatus)
                            .orElse(Status.FAILED.getValue()),
                        Status.FAILED.getValue()))
            .returnResult()
            .getResponseBody();
    Assertions.assertEquals(
        Optional.ofNullable(res).map(Response::getMessage).orElse("not found"),
        "use the correct date format: yyyy-MM-dd");
    Assertions.assertNotNull(Optional.ofNullable(res).map(Response::getErrorMessage).orElse(null));
  }

  @Test
  @DisplayName("get all foreign key relationship results, test when no query params are given")
  void testGetForeignKeyRelationshipMissingParams() {
    Response res =
        webTestClient
            .get()
            .uri("/expenses/v1")
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(Response.class)
            .consumeWith(
                responseEntityExchangeResult ->
                    Assertions.assertEquals(
                        Optional.ofNullable(responseEntityExchangeResult)
                            .map(EntityExchangeResult::getResponseBody)
                            .map(Response::getStatus)
                            .orElse(Status.FAILED.getValue()),
                        Status.FAILED.getValue()))
            .returnResult()
            .getResponseBody();
    Assertions.assertTrue(
        Optional.ofNullable(res)
            .map(Response::getErrorMessage)
            .map(s -> s.contains("startDate"))
            .orElse(false));
  }

  @Test
  @DisplayName("get all foreign key relationship results, valid response given")
  void testGetForeignKeyRelationship() {
    String startDate = "2024-09-01";
    String endDate = "2024-09-30";
    Response res =
        webTestClient
            .get()
            .uri(String.format("/expenses/v1?startDate=%s&endDate=%s", startDate, endDate))
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Response.class)
            .consumeWith(
                responseEntityExchangeResult ->
                    Assertions.assertEquals(
                        Optional.ofNullable(responseEntityExchangeResult)
                            .map(EntityExchangeResult::getResponseBody)
                            .map(Response::getStatus)
                            .orElse(Status.FAILED.getValue()),
                        Status.SUCCESS.getValue()))
            .returnResult()
            .getResponseBody();
    Assertions.assertTrue(
        Optional.ofNullable(res).map(Response::getNumberOfRecords).orElse(-1) > 0);
    Assertions.assertFalse(Optional.of(res).map(Response::getForeignKeyEntities).isEmpty());
  }
}
