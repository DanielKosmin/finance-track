package com.kosmin.finance.finance_tracker.integrationTest;

import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class InsertCreditIntegrationTest extends BaseIntegrationTest {
  @Test
  @DisplayName("test insertion into credit table")
  void testInsertionIntoBankingTable() {
    final String csvContent =
        """
    Transaction Date,Description,Category,Type,Amount
    10/10/24,Restaurant,Food & Drink,Sale,-35.00
    10/01/24,Store,Shopping,Sale,-395.87""";
    final Response response =
        makePostRequest(
            COMMON_POST_URL,
            simulateCsvFile(csvContent, "credit_records.csv"),
            HttpStatus.ACCEPTED);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(response.getStatus(), Status.SUCCESS.getValue());

    Awaitility.await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(
            () -> {
              final Response getResponse = makeGetRequest(COMMON_GET_URL + "?creditTable=true");
              Assertions.assertNotNull(getResponse);
              Assertions.assertEquals(getResponse.getRecords().size(), 2);
            });
  }

  private MultiValueMap<String, Object> simulateCsvFile(String csvContent, String fileName) {
    final ByteArrayResource resource =
        new ByteArrayResource(csvContent.getBytes()) {
          @Override
          public String getFilename() {
            return fileName;
          }
        };
    return new LinkedMultiValueMap<>() {
      {
        add("file", resource);
      }
    };
  }

  private Response makePostRequest(
      String url, MultiValueMap<String, Object> csvFile, HttpStatus httpStatus) {
    return webTestClient
        .post()
        .uri(url)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .bodyValue(csvFile)
        .exchange()
        .expectStatus()
        .isEqualTo(httpStatus)
        .expectBody(Response.class)
        .returnResult()
        .getResponseBody();
  }

  private Response makeGetRequest(String url) {
    return webTestClient
        .get()
        .uri(url)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Response.class)
        .returnResult()
        .getResponseBody();
  }
}
