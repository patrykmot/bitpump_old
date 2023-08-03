package com.vegasoft.bitpump.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BitpumpMainTests {

	@Test
	void contextLoads() {
		GenericDataSource gds = new GenericDataSourceCSV(Utils.FILE_CSV);
		GenericDataConverter gdc = new GenericDataConverterBitstamp();
		NumericData data = gdc.convert(gds.getGenericData());
		StatisticAnalyze statisticAnalyze = new StatisticAnalyze(data);
		Assertions.assertNotNull(statisticAnalyze);

		List<StatisticalResultTO> notUnique = statisticAnalyze.getNotUniqueResults();
		Assertions.assertNotNull(notUnique);

		List<double[]> notUniqueData = statisticAnalyze.getNotUniqueData();
		Assertions.assertNotNull(notUniqueData);
	}

}
