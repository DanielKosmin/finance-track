package com.kosmin.finance.finance_tracker.service;

import com.kosmin.finance.finance_tracker.model.CsvModel;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvParsingService {

  public List<CsvModel> processCsv(MultipartFile file) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      CsvToBean<CsvModel> csvToBean =
          new CsvToBeanBuilder<CsvModel>(reader)
              .withType(CsvModel.class)
              .withIgnoreLeadingWhiteSpace(true)
              .build();
      return csvToBean.parse();
    }
  }
}
