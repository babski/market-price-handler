package com.example.marketpricehandler.error;

public class InstrumentPairNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Instrument pair '%s' cannot be found";

    public InstrumentPairNotFoundException(String value) {
        super(String.format(MESSAGE, value));
    }
}
