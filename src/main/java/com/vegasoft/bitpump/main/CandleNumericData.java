package com.vegasoft.bitpump.main;

public class CandleNumericData {
    private NumericData data;

    public CandleNumericData(NumericData data) {
        this.data = data;
    }

    public void mergeCandles(int numberOFCandlesToMerge) {
        data.moveRowsIntoColumns(numberOFCandlesToMerge, (t, s) -> {
            Candle target = new Candle(data.getRow(t), data.getTimeStamp(t));
            Candle source = new Candle(data.getRow(s), data.getTimeStamp(s));
            target.merge(source);
            data.updateRow(t, target.convertToRow());
            data.updateTimeStamp(t, target.getTimeStamp());
        });
        data.doValidation();
    }

    public NumericData getNumericData() {
        return data;
    }
}
