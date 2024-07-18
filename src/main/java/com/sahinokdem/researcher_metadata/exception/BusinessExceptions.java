package com.sahinokdem.researcher_metadata.exception;

public class BusinessExceptions {
    public static BusinessException USER_NOT_FOUND = new BusinessException(ErrorCode.not_found, "User not found");
    public static BusinessException ACCOUNT_MISSING = new BusinessException(ErrorCode.account_missing, "Account missing");
    public static BusinessException INVALID_CREDENTIALS = new BusinessException(ErrorCode.validation, "Invalid credentials");
    public static BusinessException EMAIL_ALREADY_EXISTS = new BusinessException(ErrorCode.account_already_exists, "Email already exists");
    public static BusinessException AUTHORIZATION_MISSING = new BusinessException(ErrorCode.forbidden, "Not authorized to perform this action.");
    public static BusinessException FILE_NOT_FOUND = new BusinessException(ErrorCode.not_found, "File is not found");
    public static BusinessException FILE_READ_ERROR = new BusinessException(ErrorCode.internal_error, "Unable to read file");
    public static BusinessException INVALID_FILE_FORMAT_ERROR = new BusinessException(ErrorCode.forbidden, "Invalid file format");
    public static BusinessException FILE_NOT_SAVED = new BusinessException(ErrorCode.internal_error, "File not saved");
    public static BusinessException REGISTRY_ALREADY_EXIST = new BusinessException(ErrorCode.already_submitted, "Registry Type already exists");
    public static BusinessException INVALID_REGISTRY_TYPE = new BusinessException(ErrorCode.validation, "Invalid registry type");
}
