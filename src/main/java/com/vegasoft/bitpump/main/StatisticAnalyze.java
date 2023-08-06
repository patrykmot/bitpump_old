package com.vegasoft.bitpump.main;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticAnalyze {
    private NumericData data;
    private List<StatisticalResultTO> rowResults;

    private List<StatisticalResultTO> columnResults;

    private List<RealVector> rowVectors;

    private List<RealVector> columnVectors;

    private final double DELTA = 0.1;


    public StatisticAnalyze(NumericData data) {
        assert data != null;
        this.data = data;
        calculate();
    }

    public void calculate() {
        rowResults = new ArrayList<>(data.getRowCount());
        columnResults = new ArrayList<>(data.getColumnCount());
        rowVectors = new ArrayList<>(data.getRowCount());
        columnVectors = new ArrayList<>(data.getRowCount());

        // Calculate statistics for each row and then for each column in numeric data
        // Row
        for (int row = 0; row < data.getRowCount(); row++) {
            double[] rowData = data.getRow(row);
            StatisticalResultTO result = calculateStatistics(rowData);
            result.setRowNumber(row);
            rowVectors.add(new ArrayRealVector(rowData));
            rowResults.add(result);
        }
        // Column
        for (int column = 0; column < data.getColumnCount(); column++) {
            double[] columnData = data.getColumn(column);
            StatisticalResultTO result = calculateStatistics(columnData);
            result.setColumnNumber(column);
            columnVectors.add(new ArrayRealVector(columnData));
            columnResults.add(result);
        }
        calculateIfVectorsAreUnique();
    }

    private void calculateIfVectorsAreUnique() {
        calculateUniqueness(columnVectors, columnResults);
        calculateUniqueness(rowVectors, rowResults);
    }

    private void calculateUniqueness(List<RealVector> vectors, List<StatisticalResultTO> results) {
        for (int left = 0; left < vectors.size() - 1; ++left) {
//            Utils.log().info("Calculate uniqueness for {} / {} ", left, vectors.size());
            for (int right = left + 1; right < vectors.size(); ++right) {
                RealVector leftVector = vectors.get(left);
                RealVector rightVector = vectors.get(right);
                double dist = leftVector.getDistance(rightVector);
                if (dist <= DELTA) {
                    results.get(left).setUnique(false);
                    results.get(right).setUnique(false);
                }
            }
        }
    }

    private StatisticalResultTO calculateStatistics(double[] data) {

        // Calculate mean, minValue, minValue in first loop
        double mean = 0;
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        for (int i = 0; i < data.length; ++i) {
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
        for (int i = 0; i < data.length; ++i) {
            double sub = data[i] - mean;
            variance += Math.pow(sub, 2);
            standardDeviation += Math.abs(sub);
        }
        variance /= data.length;
        standardDeviation /= data.length;

        // Calculate median
        double median = calculateMedian(data);

        return new StatisticalResultTO(mean, median, variance, standardDeviation, minValue, maxValue);
    }

    private double calculateMedian(double[] data) {
        double median;
        if (data.length % 2 != 0) {
            median = data[data.length / 2];
        } else {
            int index1 = data.length / 2;
            median = (data[index1] + data[index1 + 1]) / 2.0;
        }
        return median;
    }

    public List<StatisticalResultTO> getRowResults() {
        return rowResults;
    }


    public List<StatisticalResultTO> getColumnResults() {
        return columnResults;
    }

    public List<StatisticalResultTO> getNotUniqueResults() {
        List<StatisticalResultTO> results = getNotUniqueResults(rowResults);
        results.addAll(getNotUniqueResults(columnResults));
        return results;
    }

    public List<double[]> getNotUniqueData() {
        return getNotUniqueResults().stream().map(r -> {
            if (isColumn(r)) {
                return this.data.getColumn(r.getColumnNumber());
            } else {
                return this.data.getRow(r.getRowNumber());
            }
        }).collect(Collectors.toList());
    }

    private boolean isColumn(StatisticalResultTO r) {
        return r.getColumnNumber() != null;
    }

    private boolean isRow(StatisticalResultTO r) {
        return !isColumn(r);
    }

    public List<String> getNotUniqueRowIds() {
        return getNotUniqueResults().stream().filter(this::isRow)
                .map(r -> data.getRowId(r.getRowNumber()))
                .collect(Collectors.toList());
    }


    private List<StatisticalResultTO> getNotUniqueResults(List<StatisticalResultTO> result) {
        return result.stream().filter(r -> !r.isUnique()).collect(Collectors.toList());
    }
}
