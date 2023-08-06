package com.vegasoft.bitpump.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BitpumpMainTests {

	@Test
	void contextLoads() {

		// Stage I - Load and prepare bitcoin prices
		Utils.log().info("Starting bitpump calculations.");
		GenericDataSource gds_bitcoin = new GenericDataSourceCSV(Utils.FILE_BITCOIN_CSV);
		GenericDataConverter gdc_bitcoin = new GenericDataConverterBitstamp();
		NumericData data_bitcoin = gdc_bitcoin.convert(gds_bitcoin.getGenericData());
		Utils.log().info("Starting statistical analyze.");
		StatisticAnalyze statisticAnalyze_bitcoin = new StatisticAnalyze(data_bitcoin);
		Assertions.assertNotNull(statisticAnalyze_bitcoin);

		Utils.log().info("Finding not unique results");
		List<StatisticalResultTO> notUnique = statisticAnalyze_bitcoin.getNotUniqueResults();
		Assertions.assertNotNull(notUnique);
		List<double[]> notUniqueData = statisticAnalyze_bitcoin.getNotUniqueData();
		Assertions.assertNotNull(notUniqueData);
		List<String> notUniqueRowsIds = statisticAnalyze_bitcoin.getNotUniqueRowIds();
		Assertions.assertNotNull(notUniqueRowsIds);
		Utils.log().info("Not unique rowIds {}", notUniqueRowsIds);

		Utils.log().info("Removing not unique results");
		data_bitcoin.removeRowsWithIds(notUniqueRowsIds);
		// Check again
		statisticAnalyze_bitcoin.calculate();
		notUniqueRowsIds = statisticAnalyze_bitcoin.getNotUniqueRowIds();
		Assertions.assertTrue(notUniqueRowsIds.isEmpty());

		// Stage II - Load and prepare S&P 500 prices
		GenericDataSource gds_SP500 = new GenericDataSourceCSV(Utils.FILE_SP500_CSV);
		GenericDataConverter gdc_SP500 = new GenericDataConverterSP500();
		NumericData data_SP500 = gdc_SP500.convert(gds_SP500.getGenericData());
		StatisticAnalyze statisticAnalyze_SP500 = new StatisticAnalyze(data_SP500);

	}

}
