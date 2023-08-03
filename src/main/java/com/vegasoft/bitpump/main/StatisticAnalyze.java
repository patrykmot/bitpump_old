package com.vegasoft.bitpump.main;

import java.util.ArrayList;
import java.util.List;

public class StatisticAnalyze {
    private NumericData data;

    private List<StatisticalResultTO> rowResults;
    private List<StatisticalResultTO> columnResults;

    public StatisticAnalyze(NumericData data) {
        assert data != null;
        this.data = data;
        calculate();
    }

    public void calculate() {
        rowResults = new ArrayList<>(data.getRowCount());
        columnResults = new ArrayList<>(data.getColumnCount());

        // Calculate statistics for each row and then for each column in numeric data
        // Row
        for (int row = 0; row < data.getRowCount(); row++) {
            double[] rowData = data.getRow(row);
            StatisticalResultTO result = calculateStatistics(rowData);
            rowResults.add(result);
        }
        // Column
        for (int column = 0; column < data.getColumnCount(); column++) {
            double[] columnData = data.getColumn(column);
            StatisticalResultTO result = calculateStatistics(columnData);
            columnResults.add(result);
        }
    }

    private StatisticalResultTO calculateStatistics(double[] data) {

        // Calculate mean, minValue, minValue in first loop
        double mean = 0;
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        for(int i = 0; i < data.length; ++i) {
            mean += data[i];
            if(minValue > data[i]) {
                minValue = data[i];
            }
            if(maxValue < data[i]) {
                maxValue = data[i];
            }
        }
        mean /= data.length;

        // Calculate variance, standardDeviation in next loop
        double standardDeviation = 0;
        double variance = 0;
        for(int i = 0; i < data.length; ++i) {
            double sub = data[i] - mean;
            variance += Math.pow(sub, 2);
            standardDeviation += Math.abs(sub);
        }
        variance /= data.length;
        standardDeviation /= data.length;

        // Calculate median
        double median;
        if(data.length % 2 != 0) {
            median = data[data.length / 2];
        } else {
            int index1 = data.length / 2;
            median = (data[index1] + data[index1 + 1])/2.0;
        }

        return new StatisticalResultTO(mean, median, variance, standardDeviation, minValue, maxValue);
    }

    public List<StatisticalResultTO> getRowResults() {
        return rowResults;
    }

    public void setRowResults(List<StatisticalResultTO> rowResults) {
        this.rowResults = rowResults;
    }

    public List<StatisticalResultTO> getColumnResults() {
        return columnResults;
    }

    public void setColumnResults(List<StatisticalResultTO> columnResults) {
        this.columnResults = columnResults;
    }
}
