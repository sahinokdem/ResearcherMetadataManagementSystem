package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRoleService {

    private final AuthenticationService authenticationService;

    public void assertCurrentUserRole(UserRole userRole) {
        if (!checkCurrentUserRole(userRole)) throw BusinessExceptions.AUTHORIZATION_MISSING;
    }

    public boolean checkCurrentUserRole(UserRole userRole) {
        return getCurrentUserRole().equals(userRole);
    }

    private UserRole getCurrentUserRole() {
        User currentUser = authenticationService.getAuthenticatedUser();
        return currentUser.getUserRole();
    }
}
