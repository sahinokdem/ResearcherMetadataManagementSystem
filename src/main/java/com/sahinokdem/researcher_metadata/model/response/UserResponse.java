package com.sahinokdem.researcher_metadata.model.response;

import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.ErrorDto;
import lombok.Data;

@Data
public class UserResponse {
    private final String id;
    private final String email;
    private final UserRole userRole;
}
