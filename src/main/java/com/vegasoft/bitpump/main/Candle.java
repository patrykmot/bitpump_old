package com.vegasoft.bitpump.main;

public class Candle {
    private double open, high, low, close;
    private long timestamp;

    public Candle(double[] rowInput, long timestamp) {
        assert rowInput.length == 4;
        open = rowInput[0];
        high = rowInput[1];
        low = rowInput[2];
        close = rowInput[3];
        this.timestamp = timestamp;
        validate();
    }

    public void merge(Candle candle) {
        // Take older candle as a first one
        Candle first = candle;
        Candle second = this;
        if (this.timestamp < candle.timestamp) {
            first = this;
            second = candle;
        }
        this.open = first.open;
        this.high = first.high > second.high ? first.high : second.high;
        this.low = first.low < second.low ? first.low : second.low;
        this.close = second.close;
        this.timestamp = second.timestamp;
        validate();
    }

    private void validate() {
        assert open <= high;
        assert open >= low;
        assert high >= low;
        assert close <= high;
        assert close >= low;
        assert timestamp > 0;
    }

    public double[] convertToRow() {
        return new double[]{open, high, low, close};
    }

    public long getTimeStamp() {
        return timestamp;
    }
}
