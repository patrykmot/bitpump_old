package com.vegasoft.bitpump.main;

public class BitpumpException extends RuntimeException {

    private static final String NICE_PREFIX = "Apologize, I found and error. Just please carefully check logs. All will be fine :) Message: ";

    public BitpumpException(String message) {
        super(NICE_PREFIX + message);
    }

    public BitpumpException(String message, Throwable cause) {
        super(NICE_PREFIX + message, cause);
    }

    public BitpumpException(Throwable cause) {
        super(cause);
    }

    public BitpumpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(NICE_PREFIX + message, cause, enableSuppression, writableStackTrace);
    }
}
