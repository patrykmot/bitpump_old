package com.vegasoft.bitpump.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BitpumpMainTests {

	@Test
	void contextLoads() {
		Utils.log().info("Starting bitpump calculations.");
		GenericDataSource gds = new GenericDataSourceCSV(Utils.FILE_CSV);
		GenericDataConverter gdc = new GenericDataConverterBitstamp();
		NumericData data = gdc.convert(gds.getGenericData());
		Utils.log().info("Starting statistical analyze.");
		StatisticAnalyze statisticAnalyze = new StatisticAnalyze(data);
		Assertions.assertNotNull(statisticAnalyze);

		Utils.log().info("Finding not unique results");
		List<StatisticalResultTO> notUnique = statisticAnalyze.getNotUniqueResults();
		Assertions.assertNotNull(notUnique);
		List<double[]> notUniqueData = statisticAnalyze.getNotUniqueData();
		Assertions.assertNotNull(notUniqueData);
		List<String> notUniqueRowsIds = statisticAnalyze.getNotUniqueRowIds();
		Assertions.assertNotNull(notUniqueRowsIds);
		Utils.log().info("Not unique rowIds {}", notUniqueRowsIds);

		Utils.log().info("Removing not unique results");
		data.removeRowsWithIds(notUniqueRowsIds);
		// Check again
		statisticAnalyze.calculate();
		notUniqueRowsIds = statisticAnalyze.getNotUniqueRowIds();
		Assertions.assertTrue(notUniqueRowsIds.isEmpty());


	}

}
