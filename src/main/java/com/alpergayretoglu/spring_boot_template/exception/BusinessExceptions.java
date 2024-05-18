package com.alpergayretoglu.spring_boot_template.exception;

public class BusinessExceptions {
    public static BusinessException USER_NOT_FOUND = new BusinessException(ErrorCode.not_found, "User not found");

    public static BusinessException ACCOUNT_MISSING = new BusinessException(ErrorCode.account_missing, "Account missing");

    public static BusinessException INVALID_CREDENTIALS = new BusinessException(ErrorCode.validation, "Invalid credentials");
    public static BusinessException EMAIL_ALREADY_EXISTS = new BusinessException(ErrorCode.validation, "Email already exists");
}
