package com.vegasoft.bitpump.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.apache.commons.lang3.ArrayUtils;

public class NumericData {
    private List<String> columnDescription;
    private List<double[]> rowData;
    private List<String> rowIds;
    private List<Long> rowTimeStamps;

    public NumericData(List<String> columnDescription, List<double[]> data, List<String> rowIds, List<Long> rowTimeStamps) {
        this.columnDescription = columnDescription;
        this.rowData = data;
        this.rowIds = rowIds;
        this.rowTimeStamps = rowTimeStamps;
        doValidation();
    }

    public void doValidation() {
        Set<String> setIds = new HashSet();
        setIds.addAll(rowIds);
        if (setIds.size() != rowData.size()) {
            throw new BitpumpException("Row ids are not unique!");
        }
        if (rowIds.size() != rowData.size()) {
            throw new BitpumpException("Size of ids and data are different!");
        }
        if (rowTimeStamps.size() != rowData.size()) {
            throw new BitpumpException("Size of timeStamp and data are different!");
        }
        long tPref = rowTimeStamps.get(0);
        for (int i = 1; i < rowTimeStamps.size(); ++i) {
            if (tPref < rowTimeStamps.get(i)) {
                throw new BitpumpException("Timestamps are not properly sorted! It should be descending!");
            }
            tPref = rowTimeStamps.get(i);
        }
        int dataColumnSize = rowData.get(0).length;
        rowData.forEach(d -> {
            if (d.length != dataColumnSize) {
                throw new BitpumpException("Wrong data size!");
            }
        });
        if (columnDescription.size() != dataColumnSize) {
            throw new BitpumpException("Wrong column description size!");
        }
    }

    public double[] getRow(int rowIndex) {
        return rowData.get(rowIndex);
    }

    public String getRowId(int rowIndex) {
        return rowIds.get(rowIndex);
    }

    public double[] getColumn(int columnIndex) {
        int columnLength = getRowCount();
        double[] column = new double[columnLength];

        for (int i = 0; i < rowData.size(); ++i) {
            column[i] = rowData.get(i)[columnIndex];
        }
        return column;
    }

    public int getColumnCount() {
        return rowData.get(0).length;
    }

    public int getRowCount() {
        return rowData.size();
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
        for (; left < rowData.size() && right < nd_right.rowData.size(); ++left) {
            right = nd_right.findBestIndex(rowTimeStamps.get(left), right);
            mergeRowDataAndId(left, nd_right, right);
        }
        doValidation();
    }

    private void mergeRowDataAndId(int indexTarget, NumericData ndSource, int indexSource) {
        double[] newData = ArrayUtils.addAll(rowData.get(indexTarget), ndSource.rowData.get(indexSource));
        rowData.remove(indexTarget);
        rowData.add(indexTarget, newData);

        String rowId = rowIds.get(indexTarget);
        String rowIdSource = ndSource.rowIds.get(indexSource);
        rowIds.remove(indexTarget);
        rowIds.add(indexTarget, rowId + "_" + rowIdSource);
    }

    private void mergeRowDataAndId(int indexTarget, int indexSource) {
        mergeRowDataAndId(indexTarget, this, indexSource);
        // Update timestamp, to be most recent one!
        rowTimeStamps.set(indexTarget, rowTimeStamps.get(indexSource));
    }

    public void moveRowsIntoColumns(int numberOfRowsToJoin) {
        moveRowsIntoColumns(numberOfRowsToJoin, (t, s) -> mergeRowDataAndId(t, s));
        // Update descriptions
        List<String> originalColumnDescription = new ArrayList<>(columnDescription);
        for (int i = 1; i < numberOfRowsToJoin; ++i) {
            columnDescription.addAll(originalColumnDescription);
        }
        doValidation();
    }

    public void moveRowsIntoColumns(int numberOfRowsToJoin, MergeFunction mergeFunction) {
        if (numberOfRowsToJoin <= 1) {
            // Nothing to be done!
            return;
        }
        List<Integer> rowsToBeRemoved = new ArrayList<>();
        for (int startRow = 0; startRow < rowData.size(); startRow += numberOfRowsToJoin) {
            int actualRow = startRow;
            if (rowData.size() - 1 - startRow < numberOfRowsToJoin) {
                // Remove whole row if there is not enough data to be merged
                rowsToBeRemoved.add(startRow);
            }
            for (int i = 0; i < numberOfRowsToJoin - 1 && actualRow < rowData.size() - 1; i++, actualRow++) {
                int indexSource = actualRow + 1;
                mergeFunction.merge(startRow, indexSource);

                // Since this row is copied, it's not needed any more
                rowsToBeRemoved.add(indexSource);
            }
        }
        // Remove source rows starting from last (otherwise index numbers will be wrong!)
        for (int row = rowsToBeRemoved.size() - 1; row >= 0; --row) {
            removeRow(rowsToBeRemoved.get(row));
        }
    }

    private void removeRow(int index) {
        rowIds.remove(index);
        rowData.remove(index);
        rowTimeStamps.remove(index);
    }

    private int findBestIndex(long timeStampToBeFound, int searchFromIndex) {
        // Search closes timestamps
        for (int i = searchFromIndex; i < rowTimeStamps.size(); ++i) {
            if (rowTimeStamps.get(i) <= timeStampToBeFound) {
                // This one is first smallest one
                int result = i;
                if (i > 1) {
                    // Check if previous is closer
                    long diff = Math.abs(rowTimeStamps.get(i) - timeStampToBeFound);
                    long diffPrev = Math.abs(rowTimeStamps.get(i - 1) - timeStampToBeFound);
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
        for (int i = 0; i < rowData.size(); ++i) {
            double[] row = rowData.get(i);
            for (int r = 0; r < row.length; ++r) {
                row[r] = f.apply(row[r]);
            }
        }
    }

    public long getTimeStamp(int i) {
        return rowTimeStamps.get(i);
    }

    public void updateRow(int i, double[] row) {
        assert row.length == rowData.get(0).length;
        rowData.remove(i);
        rowData.add(i, row);
    }

    public void updateTimeStamp(int i, long timeStamp) {
        rowTimeStamps.remove(i);
        rowTimeStamps.add(i, timeStamp);
    }

    public NumericData extractLastColumn() {
        int columnIndex = columnDescription.size() - 1;
        return extractColumn(columnIndex);
    }

    private static <T> List<T> getSubList(int startIndex, int lastIndex, List<T> l) {
        return new ArrayList<>(l.subList(startIndex, lastIndex));
    }

    public NumericData extractColumn(int columnIndex) {
        double[] rowValuesInColumn = getColumn(columnIndex);
        List<double[]> extractedRows = new ArrayList<>();

        for (int i = 0; i < rowData.size(); ++i) {
            extractedRows.add(new double[]{rowValuesInColumn[i]});
        }
        int rowStartIndex = 0;
        int rowLastIndex = rowData.size();
        int columnStartIndex = columnIndex;
        int columnLastIndex = columnIndex + 1;

        NumericData extractedND = extractNumericData(extractedRows, rowStartIndex, rowLastIndex, columnStartIndex, columnLastIndex);
        return extractedND;
    }

    private NumericData extractNumericData(List<double[]> extractedRows, int rowStartIndex, int rowLastIndex, int columnStartIndex, int columnLastIndex) {
        NumericData extractedND = new NumericData(getSubList(columnStartIndex, columnLastIndex, columnDescription),
                                                  getSubList(rowStartIndex, rowLastIndex, extractedRows),
                                                  getSubList(rowStartIndex, rowLastIndex, rowIds),
                                                  getSubList(rowStartIndex, rowLastIndex, rowTimeStamps));
        return extractedND;
    }

    public void removeColumn(int indexToBeRemoved) {
        columnDescription.remove(indexToBeRemoved);
        List<double[]> updatedList = new ArrayList<>(rowData.size());
        for (int i = 0; i < rowData.size(); ++i) {
            updatedList.add(removeColumnFromArray(rowData.get(i), indexToBeRemoved));
        }
        rowData.clear();
        rowData.addAll(updatedList);
        doValidation();
    }

    private double[] removeColumnFromArray(double[] doubles, int columnIndex) {
        double[] updatedValue = new double[doubles.length - 1];
        int newIndex = 0;
        for (int i = 0; i < doubles.length; ++i) {
            if (i != columnIndex) {
                updatedValue[newIndex] = doubles[i];
                newIndex++;
            }
        }
        return updatedValue;
    }

    public NumericData extractRows(int startRow, int endRow) {
        NumericData extractedND = extractNumericData(rowData, startRow, endRow, 0, columnDescription.size());
        return extractedND;
    }
}
