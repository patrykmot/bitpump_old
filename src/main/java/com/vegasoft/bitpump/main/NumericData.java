package com.vegasoft.bitpump.main;

import org.apache.commons.lang3.ArrayUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NumericData {
    private List<String> columnDescription;
    private List<double[]> data; // data row list
    private List<String> rowIds;
    private List<Long> timeStamps;

    public NumericData(List<String> columnDescription, List<double[]> data, List<String> rowIds, List<Long> timeStamps) {
        this.columnDescription = columnDescription;
        this.data = data;
        this.rowIds = rowIds;
        this.timeStamps = timeStamps;
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
        if (timeStamps.size() != data.size()) {
            throw new BitpumpException("Size of timeStamp and data are different!");
        }
        long tPref = timeStamps.get(0);
        for (int i = 1; i < timeStamps.size(); ++i) {
            if (tPref < timeStamps.get(i)) {
                throw new BitpumpException("Timestamps are not properly sorted! It should be descending!");
            }
            tPref = timeStamps.get(i);
        }
        int dataColumnSize = data.get(0).length;
        data.forEach(d -> {
            if (d.length != dataColumnSize) {
                throw new BitpumpException("Wrong data size!");
            }
        });
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
            timeStamps.remove(index);
        }
        doValidation();
    }

    public void mergeWithTimestamp(NumericData nd_right) {
        columnDescription.addAll(nd_right.columnDescription);

        // Perquisite: Timestamps are sorted in same way!
        int right = 0;
        int left = 0;
        for (; left < data.size() && right < nd_right.data.size(); ++left) {
            right = nd_right.findBestIndex(timeStamps.get(left), right);
            mergeRow(left, nd_right, right);
        }
        doValidation();
    }

    private void mergeRow(int index, NumericData ndSource, int indexSource) {
        double[] newData = ArrayUtils.addAll(data.get(index), ndSource.data.get(indexSource));
        data.remove(index);
        data.add(index, newData);

        String rowId = rowIds.get(index);
        rowIds.remove(index);
        rowIds.add(index, rowId + "_" + ndSource.rowIds.get(indexSource));
    }

    private int findBestIndex(long timeStampToBeFound, int searchFromIndex) {
        // Search closes timestamps
        for (int i = searchFromIndex; i < timeStamps.size(); ++i) {
            if (timeStamps.get(i) <= timeStampToBeFound) {
                // This one is first smallest one
                int result = i;
                if (i > 1) {
                    // Check if previous is closer
                    long diff = Math.abs(timeStamps.get(i) - timeStampToBeFound);
                    long diffPrev = Math.abs(timeStamps.get(i - 1) - timeStampToBeFound);
                    if (diff > diffPrev) {
                        result = i - 1;
                    }
                }
                return result;
            }
        }
        throw new BitpumpException("Can't find correct timestamp for " + timeStampToBeFound);
    }
}
