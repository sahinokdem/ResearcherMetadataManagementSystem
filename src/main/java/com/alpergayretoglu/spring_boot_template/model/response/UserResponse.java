package com.alpergayretoglu.spring_boot_template.model.response;

import com.alpergayretoglu.spring_boot_template.enums.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private final String id;
    private final String email;
    private final UserRole userRole;
}
