package com.alpergayretoglu.spring_boot_template.exception;

import lombok.*;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorDto {
    private ZonedDateTime timestamp;
    private int status;
    private String message;
}
