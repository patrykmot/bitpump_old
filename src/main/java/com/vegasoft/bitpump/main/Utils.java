package com.vegasoft.bitpump.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static final String FILE_BITCOIN_CSV = "data/Bitstamp_BTCUSD_1h.csv";

    public static final String FILE_BITCOIN_DAILY_CSV = "data/Bitstamp_BTCUSD_daily.csv";
    public static final String FILE_SP500_CSV = "data/SP500_HistoricalData_1691338508840.csv";

    public static Logger log() {
        return logger;
    }

    public static InputStream openFile(String fileName) {
        logger.info("Opening file {}", fileName);
        InputStream is = ClassLoader.getSystemResourceAsStream(fileName);
        if (is == null) {
            throw new BitpumpException("Can't open file " + fileName);
        }
        return is;
    }
}
