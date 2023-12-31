package com.vegasoft.bitpump.main;

public class GenericDataConverterBitstamp extends GenericDataConverter {

    // Example of input data
    //    https://www.CryptoDataDownload.com
    //    unix,date,symbol,open,high,low,close,Volume BTC,Volume USD
    //    1690588800,2023-07-29 00:00:00,BTC/USD,29316,29316,29296,29296,0.32297518,9461.880873279999
    private static final int PRICE_START_INDEX = 3;
    private static final int PRICE_STOP_INDEX = 7;


    @Override
    public NumericData convert(GenericData genericData) {
        return getNumericData(genericData, PRICE_START_INDEX, PRICE_STOP_INDEX, s -> Long.parseLong(s) * 1000, 0);
    }

    public CandleNumericData convertToCandles(GenericData genericData) {
        NumericData nd = convert(genericData);
        return new CandleNumericData(nd);
    }
}
