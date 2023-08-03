package com.vegasoft.bitpump.main;

import java.util.List;

public class NumericData {
    private List<String> columnDescription;
    private List<double[]> data; // data row list

    public NumericData(List<String> columnDescription, List<double[]> data) {
        this.columnDescription = columnDescription;
        this.data = data;
    }

    public double[] getRow(int rowIndex) {
        return data.get(rowIndex);
    }

    public double[] getColumn(int columnIndex) {
        int columnLength = getRowCount();
        double[] column = new double[columnLength];

        for(int i = 0; i < data.size() ; ++i) {
            column[i] = data.get(i)[columnIndex];
        }
        return column;
    }

    public int getColumnCount() {
        return data.get(0).length;
    }

    public int getRowCount() {
        return data.size();
    }
}
