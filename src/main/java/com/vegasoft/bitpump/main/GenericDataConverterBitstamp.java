package com.vegasoft.bitpump.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class GenericDataConverterBitstamp implements GenericDataConverter {

    // Example of input data
    //    https://www.CryptoDataDownload.com
    //    unix,date,symbol,open,high,low,close,Volume BTC,Volume USD
    //    1690588800,2023-07-29 00:00:00,BTC/USD,29316,29316,29296,29296,0.32297518,9461.880873279999
    private static final int PRICE_START_INDEX = 3;
    private static final int PRICE_STOP_INDEX = 7;

    private static final AtomicLong ids = new AtomicLong(0);

    @Override
    public NumericData convert(GenericData genericData) {
        List<double[]> data = new ArrayList<>(100);
        for (int row = 2; row < genericData.getRowCount(); ++row) {
            double[] rowArray = genericData.getRow(row).subList(PRICE_START_INDEX, PRICE_STOP_INDEX).stream().mapToDouble(s -> Double.parseDouble(s)).toArray();
            data.add(rowArray);
        }
        List<String> columnDescriptions = genericData.getRow(1).subList(PRICE_START_INDEX, PRICE_STOP_INDEX);
        List<String> rowIds = data.stream().map(d -> Long.toString(ids.incrementAndGet())).collect(Collectors.toList());
        NumericData nd = new NumericData(columnDescriptions, data, rowIds);
        return nd;
    }
}
