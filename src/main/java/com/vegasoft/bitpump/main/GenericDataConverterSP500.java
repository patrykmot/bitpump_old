package com.vegasoft.bitpump.main;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class GenericDataConverterSP500 extends GenericDataConverter {

    //        Download from https://www.nasdaq.com/market-activity/index/spx/historical
//        Date,Close/Last,Volume,Open,High,Low
//        08/04/2023,4478.03,--,4513.96,4540.34,4474.55
//        08/03/2023,4501.89,--,4494.27,4519.49,4485.54
    private static final int PRICE_START_INDEX = 1;
    private static final int PRICE_STOP_INDEX = 2;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private static final Function<String, Long> parseTimestamp = s -> {
        try {
            return sdf.parse(s).getTime();
        } catch (Exception e) {
            throw new BitpumpException("Can't parse date from " + s, e);
        }
    };

    private static final AtomicLong ids = new AtomicLong(0);

    @Override
    public NumericData convert(GenericData genericData) {
        return getNumericData(genericData, PRICE_START_INDEX, PRICE_STOP_INDEX, parseTimestamp, 0);
    }


}
