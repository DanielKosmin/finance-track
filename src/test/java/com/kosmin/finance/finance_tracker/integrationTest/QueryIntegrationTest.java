package com.kosmin.finance.finance_tracker.integrationTest;

import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

public class QueryIntegrationTest extends BaseIntegrationTest {

  @Test
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
}
