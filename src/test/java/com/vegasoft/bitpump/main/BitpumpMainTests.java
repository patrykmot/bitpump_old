package com.vegasoft.bitpump.main;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BitpumpMainTests {

	@Test
	void contextLoads() {
		GenericDataSource gds = new GenericDataSourceCSV(Utils.FILE_CSV);
		GenericDataConverter gdc = new GenericDataConverterBitstamp();
		NumericData data = gdc.convert(gds.getGenericData());
		StatisticAnalyze statisticAnalyze = new StatisticAnalyze(data);

		statisticAnalyze.getColumnResults();


	}

}
