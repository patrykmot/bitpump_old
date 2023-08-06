package com.vegasoft.bitpump.main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NumericData {
    private List<String> columnDescription;
    private List<double[]> data; // data row list
    private List<String> rowIds;
    private List<Long> timeStamp;

    public NumericData(List<String> columnDescription, List<double[]> data, List<String> rowIds, List<Long> timeStamp) {
        this.columnDescription = columnDescription;
        this.data = data;
        this.rowIds = rowIds;
        this.timeStamp = timeStamp;
        doValidation();
    }

    private void doValidation() {
        Set<String> setIds = new HashSet();
        setIds.addAll(rowIds);
        if (setIds.size() != data.size()) {
            throw new BitpumpException("Row ids are not unique!");
        }
        if (rowIds.size() != data.size()) {
            throw new BitpumpException("Size of ids and data are different!");
        }
        if (timeStamp.size() != data.size()) {
            throw new BitpumpException("Size of timeStamp and data are different!");
        }
    }

    public double[] getRow(int rowIndex) {
        return data.get(rowIndex);
    }

    public String getRowId(int rowIndex) {
        return rowIds.get(rowIndex);
    }

    public double[] getColumn(int columnIndex) {
        int columnLength = getRowCount();
        double[] column = new double[columnLength];

        for (int i = 0; i < data.size(); ++i) {
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

    public void removeRowsWithIds(List<String> notUniqueRowsIds) {
        for (int i = 0; i < notUniqueRowsIds.size(); ++i) {
            int index = rowIds.indexOf(notUniqueRowsIds.get(i));
            rowIds.remove(index);
            data.remove(index);
            timeStamp.remove(index);
        }
        doValidation();
    }
}
