package edu.ut.its.exceptions;


import ch.qos.logback.core.spi.ErrorCodes;

public class AppException extends RuntimeException {

    public AppException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
