package com.kosmin.finance.finance_tracker.domain.update;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.domain.query.QueryParentRequestId;
import com.kosmin.finance.finance_tracker.model.Response;
import com.kosmin.finance.finance_tracker.model.Status;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
public class UpdateForeignKeyMappingTest {
  private static final String UPDATE_FOREIGN_KEY = "update-foreign-key-with-parent-id";
  @Mock private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Mock private SqlQueriesConfig sqlQueriesConfig;
  @Mock private QueryParentRequestId queryParentRequestId;
  private UpdateForeignKey update;
  Map<String, String> map = new HashMap<>();

  @BeforeEach
  void setUp() {
    final String updateForeignKey =
        loadYamlProperties("queries.yml").getProperty("queries.map." + UPDATE_FOREIGN_KEY);
    map.put(UPDATE_FOREIGN_KEY, updateForeignKey);
    Mockito.when(sqlQueriesConfig.getMap()).thenReturn(map);
    update =
        new UpdateForeignKey(namedParameterJdbcTemplate, sqlQueriesConfig, queryParentRequestId);
  }

  @Test
  @DisplayName("create foreign key relationship")
  void updateForeignKey() {
    final TransactionMappingRequest transactionMappingRequest =
        TransactionMappingRequest.builder()
            .transactionStartDate("2024-10-01")
            .transactionEndDate("2024-10-31")
            .transactionDescription("%chase%")
            .transactionType("credit")
            .build();
    Mockito.when(queryParentRequestId.getParentRequestId(transactionMappingRequest)).thenReturn(1);
    Mockito.when(
            namedParameterJdbcTemplate.update(
                sqlQueriesConfig.getMap().get(UPDATE_FOREIGN_KEY),
                Map.of(
                    "startDate", transactionMappingRequest.getTransactionStartDate(),
                    "endDate", transactionMappingRequest.getTransactionEndDate(),
                    "parentId", 1,
                    "type", transactionMappingRequest.getTransactionType())))
        .thenReturn(1);

    final var res = update.createTableRelationship(transactionMappingRequest);
    Assertions.assertEquals(
        res,
        Response.builder()
            .status(Status.SUCCESS.getValue())
            .message(String.format("Updated Foreign Key value for %s rows", 1))
            .build());
  }

  private Properties loadYamlProperties(String ymlFile) {
    final Resource resource = new ClassPathResource(ymlFile);
    final YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
    yamlPropertiesFactoryBean.setResources(resource);
    return yamlPropertiesFactoryBean.getObject();
  }
}
