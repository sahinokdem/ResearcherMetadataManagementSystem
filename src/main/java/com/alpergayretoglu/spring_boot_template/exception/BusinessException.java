package com.alpergayretoglu.spring_boot_template.exception;

public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public BusinessException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getStatusCode() {
        return errorCode.getHttpCode();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
