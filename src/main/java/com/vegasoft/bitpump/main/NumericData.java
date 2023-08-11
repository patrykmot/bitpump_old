package com.vegasoft.bitpump.main;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

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
        if (columnDescription.size() != dataColumnSize) {
            throw new BitpumpException("Wrong column description size!");
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

    public void removeRowsWithIds(List<String> rowsToBeRemoved) {
        for (int i = 0; i < rowsToBeRemoved.size(); ++i) {
            int index = rowIds.indexOf(rowsToBeRemoved.get(i));
            removeRow(index);
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
            mergeRowDataAndId(left, nd_right, right);
        }
        doValidation();
    }

    private void mergeRowDataAndId(int indexTarget, NumericData ndSource, int indexSource) {
        double[] newData = ArrayUtils.addAll(data.get(indexTarget), ndSource.data.get(indexSource));
        data.remove(indexTarget);
        data.add(indexTarget, newData);

        String rowId = rowIds.get(indexTarget);
        String rowIdSource = ndSource.rowIds.get(indexSource);
        rowIds.remove(indexTarget);
        rowIds.add(indexTarget, rowId + "_" + rowIdSource);
    }

    public void moveRowsIntoColumns(int numberOfRowsToJoin) {
        if (numberOfRowsToJoin <= 1) {
            // Nothing to be done!
            return;
        }

        List<Integer> rowsToBeRemoved = new ArrayList<>();
        for (int startRow = 0; startRow < data.size(); startRow += numberOfRowsToJoin) {
            int actualRow = startRow;
            if (data.size() - 1 - startRow < numberOfRowsToJoin) {
                // Remove whole row if there is not enough data to be merged
                rowsToBeRemoved.add(startRow);
            }
            for (int i = 0; i < numberOfRowsToJoin - 1 && actualRow < data.size() - 1; i++, actualRow++) {
                int indexSource = actualRow + 1;
                mergeRowDataAndId(startRow, this, indexSource);
                // Since this row is copied, it's not needed any more
                rowsToBeRemoved.add(indexSource);
            }
        }
        // Remove source rows starting from last (otherwise index numbers will be wrong!)
        for (int row = rowsToBeRemoved.size() - 1; row >= 0; --row) {
            removeRow(rowsToBeRemoved.get(row));
        }

        // Update descriptions
        List<String> originalColumnDescription = new ArrayList<>(columnDescription);
        for (int i = 1; i < numberOfRowsToJoin; ++i) {
            columnDescription.addAll(originalColumnDescription);
        }

        doValidation();
    }

    private void removeRow(int index) {
        rowIds.remove(index);
        data.remove(index);
        timeStamps.remove(index);
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

    public void applyFunctionOnAllData(Function<Double, Double> f) {
        for (int i = 0; i < data.size(); ++i) {
            double[] row = data.get(i);
            for (int r = 0; r < row.length; ++r) {
                row[r] = f.apply(row[r]);
            }
        }
    }

}
