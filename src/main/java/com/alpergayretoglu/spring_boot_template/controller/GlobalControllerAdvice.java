package com.alpergayretoglu.spring_boot_template.controller;

import com.alpergayretoglu.spring_boot_template.exception.BusinessException;
import com.alpergayretoglu.spring_boot_template.exception.ErrorDto;
import com.alpergayretoglu.spring_boot_template.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> customHandleBusinessException(
            BusinessException ex,
            WebRequest request
    ) {
        LOGGER.info("Business Error: {}", ex.getMessage());

        ErrorDto error = new ErrorDto();
        error.setTimestamp(DateUtil.now());
        error.setStatus(ex.getStatusCode());
        error.setMessage(ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> constraintViolationException(
            ConstraintViolationException ex,
            WebRequest request
    ) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        ErrorDto error = new ErrorDto();
        error.setTimestamp(DateUtil.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(String.join(", ", errors));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @Nullable HttpHeaders headers,
            HttpStatus status,
            @Nullable WebRequest request
    ) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorDto error = new ErrorDto();
        error.setTimestamp(DateUtil.now());
        error.setStatus(status.value());
        error.setMessage(String.join(", ", errors));

        return new ResponseEntity<>(error, headers, status);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex,
            @Nullable HttpHeaders headers,
            HttpStatus status,
            @Nullable WebRequest request
    ) {
        ErrorDto error = new ErrorDto();
        error.setTimestamp(DateUtil.now());
        error.setStatus(status.value());
        error.setMessage(ex.getRequestPartName() + " is missing!");

        return new ResponseEntity<>(error, headers, status);
    }
}
