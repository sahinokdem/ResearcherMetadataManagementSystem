package com.sahinokdem.researcher_metadata.model.response;

import com.sahinokdem.researcher_metadata.enums.UserRole;
import lombok.Data;

@Data
public class LoginResponse {
    private final String id;
    private final String token;
    private final UserRole userRole;
}
