package com.alpergayretoglu.spring_boot_template.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    unknown(400),
    not_found(404),
    validation(422),
    unauthorized(401),
    forbidden(403),
    resource_missing(404),
    account_already_exists(409),
    account_missing(404),
    password_mismatch(409),
    account_already_verified(403),
    account_not_verified(403),
    code_expired(410),
    code_mismatch(409),
    already_onboarded(409),
    insufficient_balance(409),
    conflict(409),
    already_submitted(409),
    internal_error(500);

    private final int httpCode;
}
