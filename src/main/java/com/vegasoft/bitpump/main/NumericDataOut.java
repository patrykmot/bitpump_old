package com.vegasoft.bitpump.main;

import java.io.File;

public class NumericDataOut {

    private static final String DATA_OUT = "DATA_OUT";
    private CSVDoor loader = new CSVDoor();

    public NumericDataOut() {
        File directory = new File(DATA_OUT);
        if (!directory.isDirectory() || !directory.exists()) {
            if (!directory.mkdir()) {
                throw new BitpumpException("Can't create folder " + DATA_OUT);
            }
        }
    }

    public void save(NumericData numericData, String fileName) {
        loader.saveCSV(DATA_OUT + "/" + fileName, numericData);
    }
}
