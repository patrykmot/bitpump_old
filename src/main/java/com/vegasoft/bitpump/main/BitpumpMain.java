package com.vegasoft.bitpump.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BitpumpMain {

	public static void main(String[] args) {
		SpringApplication.run(BitpumpMain.class, args);

		GenericDataSource gds = new GenericDataSourceCSV(Utils.FILE_CSV);
//		GenericDataConverter gdc =

	}

}
