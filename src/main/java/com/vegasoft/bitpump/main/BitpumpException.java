package com.vegasoft.bitpump.main;

public class BitpumpException extends RuntimeException {
    public BitpumpException(String message) {
        super(message);
    }

    public BitpumpException(String message, Throwable cause) {
        super(message, cause);
    }

    public BitpumpException(Throwable cause) {
        super(cause);
    }

    public BitpumpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
