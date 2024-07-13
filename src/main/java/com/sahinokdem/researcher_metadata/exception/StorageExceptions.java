package com.sahinokdem.researcher_metadata.exception;

public class StorageExceptions {
    public static StorageException LOCATION_NULL_ERROR = new StorageException(ErrorCode.internal_error, "Location is null");
    public static StorageException FILE_NOT_FOUND = new StorageException(ErrorCode.not_found, "File is not found");
    public static StorageException FILE_READ_ERROR = new StorageException(ErrorCode.internal_error, "Unable to read file");
    public static StorageException INVALID_FILE_FORMAT_ERROR = new StorageException(ErrorCode.forbidden, "Invalid file format");
    public static BusinessException FILE_NOT_SAVED = new BusinessException(ErrorCode.internal_error, "File not saved");
}
