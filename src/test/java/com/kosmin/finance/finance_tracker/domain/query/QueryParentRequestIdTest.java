package com.kosmin.finance.finance_tracker.domain.query;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.model.TransactionMappingRequest;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
public class QueryParentRequestIdTest {
  private static final String GET_PARENT_ID = "get-parent-id-of-transaction";
  @Mock private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Mock private SqlQueriesConfig sqlQueriesConfig;
  private QueryParentRequestId queryId;
  Map<String, String> map = new HashMap<>();

  @BeforeEach
  void setUp() {
    final String query =
        loadYamlProperties("queries.yml").getProperty("queries.map." + GET_PARENT_ID);
    map.put(GET_PARENT_ID, query);
    Mockito.when(sqlQueriesConfig.getMap()).thenReturn(map);
    queryId = new QueryParentRequestId(namedParameterJdbcTemplate, sqlQueriesConfig);
  }

  @Test
  @DisplayName("get valid parent request id")
  void getForeignKeyRelationshipEmptyResult() {
    final TransactionMappingRequest transactionMappingRequest =
        TransactionMappingRequest.builder()
            .transactionStartDate("2024-10-01")
            .transactionEndDate("2024-10-31")
            .transactionDescription("%chase%")
            .transactionType("credit")
            .build();
    Mockito.when(
            namedParameterJdbcTemplate.query(
                Mockito.eq(sqlQueriesConfig.getMap().get(GET_PARENT_ID)),
                Mockito.eq(
                    Map.of(
                        "startDate",
                        "2024-11-01",
                        "endDate",
                        "2024-11-30",
                        "description",
                        "%chase%",
                        "type",
                        "Debit")),
                Mockito.<RowMapper<Integer>>any()))
        .thenReturn(List.of(1));
    final var res = queryId.getParentRequestId(transactionMappingRequest);
    Assertions.assertEquals(res, 1);
  }

  private Properties loadYamlProperties(String ymlFile) {
    final Resource resource = new ClassPathResource(ymlFile);
    final YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
    yamlPropertiesFactoryBean.setResources(resource);
    return yamlPropertiesFactoryBean.getObject();
  }
}
