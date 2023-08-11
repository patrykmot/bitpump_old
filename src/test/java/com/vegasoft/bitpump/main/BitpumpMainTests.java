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
		StatisticAnalyze statisticAnalyzeBTC = new StatisticAnalyze(data_bitcoin);
		Assertions.assertNotNull(statisticAnalyzeBTC);

		Utils.log().info("Finding not unique results");
		List<StatisticalResultTO> notUnique = statisticAnalyzeBTC.getNotUniqueResults();
		Assertions.assertNotNull(notUnique);
		List<double[]> notUniqueData = statisticAnalyzeBTC.getNotUniqueData();
		Assertions.assertNotNull(notUniqueData);
		List<String> notUniqueRowsIds = statisticAnalyzeBTC.getNotUniqueRowIds();
		Assertions.assertNotNull(notUniqueRowsIds);
		Utils.log().info("Not unique rowIds {}", notUniqueRowsIds);

		Utils.log().info("Removing not unique results");
		data_bitcoin.removeRowsWithIds(notUniqueRowsIds);
		// Check again
		statisticAnalyzeBTC.calculate();
		notUniqueRowsIds = statisticAnalyzeBTC.getNotUniqueRowIds();
		Assertions.assertTrue(notUniqueRowsIds.isEmpty());
		Utils.log().info("Move rows into columns");
		data_bitcoin.moveRowsIntoColumns(5);
		statisticAnalyzeBTC.normalizeData();

		// Stage II - Load and prepare S&P 500 prices
		Utils.log().info("Merging bitcoin data with S&P 500");
		GenericDataSource gds_SP500 = new GenericDataSourceCSV(Utils.FILE_SP500_CSV);
		GenericDataConverter gdc_SP500 = new GenericDataConverterSP500();
		NumericData data_SP500 = gdc_SP500.convert(gds_SP500.getGenericData());
		StatisticAnalyze statisticAnalyzeSP500 = new StatisticAnalyze(data_SP500);
		statisticAnalyzeSP500.normalizeData();

		// Merge S&P 500 into Bitcoin data
		data_bitcoin.mergeWithTimestamp(data_SP500);
		Utils.log().info("Done!");


	}

}
