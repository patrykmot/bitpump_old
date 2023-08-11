package com.vegasoft.bitpump.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class GenericDataConverter {

    private static final int INITIAL_CAPACITY = 100;

    private static final AtomicLong ids = new AtomicLong(0);

    abstract NumericData convert(GenericData genericData);

    protected NumericData getNumericData(GenericData genericData, int priceStartIndex, int priceStopIndex, Function<String, Long> timestampFunction, int timestampIndex) {
        List<double[]> data = new ArrayList<>(INITIAL_CAPACITY);
        List<Long> timestamps = new ArrayList<>(INITIAL_CAPACITY);
        for (int row = 2; row < genericData.getRowCount(); ++row) {
            // Add row
            List<String> rowValues = genericData.getRow(row);
            double[] rowArray = rowValues.subList(priceStartIndex, priceStopIndex).stream().mapToDouble(s -> Double.parseDouble(s)).toArray();
            data.add(rowArray);

            // Add timestamp
            timestamps.add(timestampFunction.apply(rowValues.get(timestampIndex)));
        }
        List<String> columnDescriptions = genericData.getRow(1).subList(priceStartIndex, priceStopIndex);
        List<String> rowIds = data.stream().map(d -> Long.toString(ids.incrementAndGet())).collect(Collectors.toList());
        NumericData nd = new NumericData(columnDescriptions, data, rowIds, timestamps);
        return nd;
    }
}
