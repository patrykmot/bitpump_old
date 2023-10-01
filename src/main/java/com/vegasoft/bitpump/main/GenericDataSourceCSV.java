package com.vegasoft.bitpump.main;


public class GenericDataSourceCSV implements GenericDataSource {
    private String path;
    private CSVDoor loader;

    public GenericDataSourceCSV(String path) {
        this.path = path;
        this.loader = new CSVDoor();
    }

    @Override
    public GenericData getGenericData() {
        return loader.readCSV(path);
    }

}
