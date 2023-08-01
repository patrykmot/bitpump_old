package com.vegasoft.bitpump.main;

import com.opencsv.CSVReader;
import org.yaml.snakeyaml.reader.StreamReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class CSVLoader {
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
            e.printStackTrace();
        }
        return gd;
    }
}
