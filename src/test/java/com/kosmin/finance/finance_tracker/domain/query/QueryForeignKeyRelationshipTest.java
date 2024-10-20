package com.kosmin.finance.finance_tracker.domain.query;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.exception.ForeignKeyRelationshipNotFoundException;
import com.kosmin.finance.finance_tracker.model.ForeignKeyEntity;
import com.kosmin.finance.finance_tracker.model.Status;
import java.util.Collections;
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
public class QueryForeignKeyRelationshipTest {

  private static final String GET_FOREIGN_KEY_RELATIONSHIP_KEY = "get-foreign-key-relationship";
  @Mock private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Mock private SqlQueriesConfig sqlQueriesConfig;
  private QueryForeignKeyRelationship queryForeignKeyRelationship;
  Map<String, String> map = new HashMap<>();

  @BeforeEach
  void setUp() {
    final String query =
        loadYamlProperties("queries.yml")
            .getProperty("queries.map." + GET_FOREIGN_KEY_RELATIONSHIP_KEY);
    map.put(GET_FOREIGN_KEY_RELATIONSHIP_KEY, query);
    Mockito.when(sqlQueriesConfig.getMap()).thenReturn(map);
    queryForeignKeyRelationship =
        new QueryForeignKeyRelationship(namedParameterJdbcTemplate, sqlQueriesConfig);
  }

  @Test
  @DisplayName("Should throw exception when no foreign key relationship found")
  void getForeignKeyRelationshipEmptyResult() {
    final String startDate = "2024-10-01";
    final String endDate = "2024-10-31";
    Mockito.when(
            namedParameterJdbcTemplate.query(
                Mockito.eq(sqlQueriesConfig.getMap().get(GET_FOREIGN_KEY_RELATIONSHIP_KEY)),
                Mockito.eq(Map.of("startDate", startDate, "endDate", endDate)),
                Mockito.<RowMapper<ForeignKeyEntity>>any()))
        .thenReturn(Collections.emptyList());

    final var thrownRes =
        Assertions.assertThrows(
            ForeignKeyRelationshipNotFoundException.class,
            () -> queryForeignKeyRelationship.getForeignKeyRelationship(startDate, endDate));

    Assertions.assertEquals(
        thrownRes.getMessage(),
        String.format(
            "No foreign key relationship found for date range: Start Date: %s, End Date: %s",
            startDate, endDate));
  }

  @Test
  @DisplayName("Should return foreign key relationship when found")
  void getForeignKeyRelationship() {
    final List<ForeignKeyEntity> entities =
        List.of(
            ForeignKeyEntity.builder()
                .creditTransactionDate("")
                .creditTransactionDescription("")
                .creditTransactionCategory("")
                .creditTransactionAmount(1.0)
                .checkingTransactionAmount(1.0)
                .checkingTransactionDate("")
                .build());
    final String startDate = "2024-10-01";
    final String endDate = "2024-10-31";
    Mockito.when(
            namedParameterJdbcTemplate.query(
                Mockito.eq(sqlQueriesConfig.getMap().get(GET_FOREIGN_KEY_RELATIONSHIP_KEY)),
                Mockito.eq(Map.of("startDate", startDate, "endDate", endDate)),
                Mockito.<RowMapper<ForeignKeyEntity>>any()))
        .thenReturn(entities);

    final var res = queryForeignKeyRelationship.getForeignKeyRelationship(startDate, endDate);

    Assertions.assertEquals(res.getStatus(), Status.SUCCESS.getValue());
    Assertions.assertEquals(res.getNumberOfRecords(), 1);
    Assertions.assertEquals(res.getForeignKeyEntities(), entities);
  }

  private Properties loadYamlProperties(String ymlFile) {
    final Resource resource = new ClassPathResource(ymlFile);
    final YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
    yamlPropertiesFactoryBean.setResources(resource);
    return yamlPropertiesFactoryBean.getObject();
  }
}
