package com.kosmin.finance.finance_tracker.domain.insert;

import com.kosmin.finance.finance_tracker.config.SqlQueriesConfig;
import com.kosmin.finance.finance_tracker.model.BankingAccountModel;
import com.kosmin.finance.finance_tracker.model.CreditCardRecordsModel;
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
public class InsertIntoTablesTest {

  private static final String INSERT_INTO_BANKING_TABLE = "insert-banking-records";
  private static final String INSERT_INTO_CREDIT_TABLE = "insert-credit-records";
  @Mock private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Mock private SqlQueriesConfig sqlQueriesConfig;
  private InsertTableRecords insertTableRecords;
  Map<String, String> map = new HashMap<>();

  @BeforeEach
  void setUp() {
    String bankingQuery =
        loadYamlProperties("queries.yml").getProperty("queries.map." + INSERT_INTO_BANKING_TABLE);
    String creditQuery =
        loadYamlProperties("queries.yml").getProperty("queries.map." + INSERT_INTO_CREDIT_TABLE);
    map.put(INSERT_INTO_BANKING_TABLE, bankingQuery);
    map.put(INSERT_INTO_CREDIT_TABLE, creditQuery);
    Mockito.when(sqlQueriesConfig.getMap()).thenReturn(map);
    insertTableRecords = new InsertTableRecords(namedParameterJdbcTemplate, sqlQueriesConfig);
  }

  @Test
  @DisplayName("insert records into banking table")
  void insertRecordsIntoBankingTable() {
    Mockito.when(
            namedParameterJdbcTemplate.update(
                Mockito.eq(sqlQueriesConfig.getMap().get(INSERT_INTO_BANKING_TABLE)),
                Mockito.anyMap()))
        .thenReturn(1);
    Assertions.assertDoesNotThrow(
        () -> insertTableRecords.insertBankingInformation(Mockito.mock(BankingAccountModel.class)));
    Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1))
        .update(Mockito.anyString(), Mockito.anyMap());
  }

  @Test
  @DisplayName("insert records into credit table")
  void insertRecordsIntoCreditTable() {
    Mockito.when(
            namedParameterJdbcTemplate.update(
                Mockito.eq(sqlQueriesConfig.getMap().get(INSERT_INTO_CREDIT_TABLE)),
                Mockito.anyMap()))
        .thenReturn(1);
    Assertions.assertDoesNotThrow(
        () ->
            insertTableRecords.insertCreditInformation(Mockito.mock(CreditCardRecordsModel.class)));
    Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1))
        .update(Mockito.anyString(), Mockito.anyMap());
  }

  private Properties loadYamlProperties(String ymlFile) {
    Resource resource = new ClassPathResource(ymlFile);
    YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
    yamlPropertiesFactoryBean.setResources(resource);
    return yamlPropertiesFactoryBean.getObject();
  }
}
