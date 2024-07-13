package com.sahinokdem.researcher_metadata.exception;

public class StorageException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;

    public StorageException(ErrorCode errorCode, String message) {
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
