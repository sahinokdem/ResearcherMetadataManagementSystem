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
    public static BusinessException REGISTRY_NAME_ALREADY_EXIST = new BusinessException(ErrorCode.already_submitted, "Registry name already exists");
    public static BusinessException INVALID_REGISTRY_TYPE = new BusinessException(ErrorCode.validation, "Invalid registry type");
    public static BusinessException REGISTRY_NOT_FOUND = new BusinessException(ErrorCode.not_found, "Registry not found");
    public static BusinessException METADATA_VALUE_INVALID = new BusinessException(ErrorCode.validation, "Metadata value invalid");
    public static BusinessException REGISTRY_TYPE_NOT_FOUND = new BusinessException(ErrorCode.internal_error, "Registry type not found");
    public static BusinessException NON_RESEARCHER_WITH_METADATA  = new BusinessException(ErrorCode.internal_error, "User is not researcher and has metadata");
    public static BusinessException METADATA_VALUE_NOT_FOUND = new BusinessException(ErrorCode.not_found, "Metadata value not found");
    public static BusinessException FORM_NOT_FOUND = new BusinessException(ErrorCode.not_found, "Form not found");
    public static BusinessException CV_NOT_FOUND = new BusinessException(ErrorCode.not_found, "CV not found");
    public static BusinessException WAITING_APPLICATION_EXIST = new BusinessException(ErrorCode.already_submitted, "Waiting application already exists");
    public static BusinessException APPLICATION_ACCEPTED = new BusinessException(ErrorCode.forbidden, "Previous application already accepted");
    public static BusinessException APPLICATION_PENALTY = new BusinessException(ErrorCode.forbidden, "Penalty to apply action has not finished");
}
