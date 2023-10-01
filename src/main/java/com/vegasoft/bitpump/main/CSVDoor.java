package com.vegasoft.bitpump.main;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;


public class CSVDoor {
    public GenericData readCSV(String path) {
        GenericData gd = new GenericData();
        InputStream stream = Utils.openFile(path);
        try (CSVReader reader = new CSVReader(new InputStreamReader(stream))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                gd.newRow();
                for (String column : nextLine) {
                    gd.addColumn(column);
                }
            }
        } catch (IOException e) {
            throw new BitpumpException("Can't parse file " + path, e);
        }
        Utils.log().info("Parsed {} rows from file.", gd.getRowCount());
        return gd;
    }

    public void saveCSV(String path, NumericData numericData) {
        try {
            OutputStream outputStream = Utils.openFileToWrite(path);
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream), ',', ' ');
            writer.writeNext(castToStringArray(numericData.getColumnDescriptions()));
            numericData.forEachDataRow(dataRow -> writer.writeNext(castToStringArray(dataRow)));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't save file " + path, e);
        }
    }

    private String[] castToStringArray(double[] dataRow) {
        String[] str = new String[dataRow.length];
        for (int i = 0; i < dataRow.length; ++i) {
            str[i] = Double.toString(dataRow[i]);
        }
        return str;
    }

    private String[] castToStringArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }
}
