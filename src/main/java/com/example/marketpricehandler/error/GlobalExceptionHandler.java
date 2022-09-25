package com.example.marketpricehandler.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
class GlobalExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(InstrumentPairNotFoundException.class)
    Error handleInstrumentPairNotFoundException(InstrumentPairNotFoundException exception) {
        log.warn(exception.getMessage());
        return new Error(NOT_FOUND.value(), NOT_FOUND.name(), exception.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    Error handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new Error(INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR.name(), null);
    }
}
