package com.vegasoft.bitpump.main;

public class StatisticalResultTO {
    private double mean;
    private double median;
    private double variance;
    private double standardDeviation;
    private double minValue;
    private double maxValue;
    private Integer rowNumber;
    private Integer columnNumber;
    private boolean unique = true;

    public StatisticalResultTO(double mean, double median, double variance, double standardDeviation, double minValue, double maxValue) {
        this.mean = mean;
        this.median = median;
        this.variance = variance;
        this.standardDeviation = standardDeviation;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(Integer columnNumber) {
        this.columnNumber = columnNumber;
    }

    public double getMean() {
        return mean;
    }

    public double getMedian() {
        return median;
    }

    public double getVariance() {
        return variance;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @Override
    public String toString() {
        return "StatisticalResultTO{" +
                "mean=" + mean +
                ", median=" + median +
                ", variance=" + variance +
                ", standardDeviation=" + standardDeviation +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", unique=" + unique +
                '}';
    }
}
