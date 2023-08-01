package com.vegasoft.bitpump.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static InputStream openFile(String fileName) {
        logger.info("Opening file {}", fileName);
        InputStream is = ClassLoader.getSystemResourceAsStream(fileName);
        if(is == null) {
            throw new BitpumpException("Can't open file " + fileName);
        }
        return is;
    }
}
