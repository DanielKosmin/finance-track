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
    final Properties properties = loadYamlProperties("queries.yml");

    // Fetch the SQL queries from YAML and ensure they are not null
    final String bankingQuery = properties.getProperty("queries.map." + INSERT_INTO_BANKING_TABLE);
    final String creditQuery = properties.getProperty("queries.map." + INSERT_INTO_CREDIT_TABLE);

    // Ensure we add these to the map
    map.put(INSERT_INTO_BANKING_TABLE, bankingQuery);
    map.put(INSERT_INTO_CREDIT_TABLE, creditQuery);

    // Inject into insertTableRecords
    insertTableRecords = new InsertTableRecords(namedParameterJdbcTemplate, sqlQueriesConfig);
  }

  @Test
  @DisplayName("insert records into banking table")
  void insertRecordsIntoBankingTable() {
    final BankingAccountModel bankingAccountModel =
        BankingAccountModel.builder()
            .transactionDescription("")
            .transactionDate("")
            .transactionType("")
            .transactionAmount(0.0)
            .balance(0.0)
            .build();
    final Map<String, Object> params = new HashMap<>();
    params.put("transactionDescription", bankingAccountModel.getTransactionDescription());
    params.put("transactionDate", bankingAccountModel.getTransactionDate());
    params.put("transactionType", bankingAccountModel.getTransactionType());
    params.put("transactionAmount", bankingAccountModel.getTransactionAmount());
    params.put("balance", bankingAccountModel.getBalance());
    Mockito.when(
            namedParameterJdbcTemplate.update(
                sqlQueriesConfig.getMap().get(INSERT_INTO_BANKING_TABLE), params))
        .thenReturn(1);
    Assertions.assertDoesNotThrow(
        () -> insertTableRecords.insertBankingInformation(bankingAccountModel));
    Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1))
        .update(sqlQueriesConfig.getMap().get(INSERT_INTO_BANKING_TABLE), params);
  }

  @Test
  @DisplayName("insert records into credit table")
  void insertRecordsIntoCreditTable() {
    final CreditCardRecordsModel creditCardRecordsModel =
        CreditCardRecordsModel.builder()
            .transactionAmount(0.0)
            .transactionType("")
            .transactionCategory("")
            .transactionDescription("")
            .transactionDate("")
            .build();
    final Map<String, Object> params = new HashMap<>();
    params.put("transactionDate", creditCardRecordsModel.getTransactionDate());
    params.put("transactionDescription", creditCardRecordsModel.getTransactionDescription());
    params.put("transactionCategory", creditCardRecordsModel.getTransactionCategory());
    params.put("transactionType", creditCardRecordsModel.getTransactionType());
    params.put("transactionAmount", creditCardRecordsModel.getTransactionAmount());
    Mockito.when(
            namedParameterJdbcTemplate.update(
                sqlQueriesConfig.getMap().get(INSERT_INTO_CREDIT_TABLE), params))
        .thenReturn(1);
    Assertions.assertDoesNotThrow(
        () -> insertTableRecords.insertCreditInformation(creditCardRecordsModel));
    Mockito.verify(namedParameterJdbcTemplate, Mockito.times(1))
        .update(sqlQueriesConfig.getMap().get(INSERT_INTO_CREDIT_TABLE), params);
  }

  private Properties loadYamlProperties(String ymlFile) {
    final Resource resource = new ClassPathResource(ymlFile);
    final YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
    yamlPropertiesFactoryBean.setResources(resource);
    return yamlPropertiesFactoryBean.getObject();
  }
}
