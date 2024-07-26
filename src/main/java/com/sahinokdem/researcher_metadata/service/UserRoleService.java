package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRoleService {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public void assertCurrentUserRole(UserRole userRole) {
        if (!checkCurrentUserRole(userRole)) throw BusinessExceptions.AUTHORIZATION_MISSING;
    }

    public boolean checkCurrentUserRole(UserRole userRole) {
        return getCurrentUserRole().equals(userRole);
    }

    public boolean checkSpecificUserRole(String userId, UserRole userRole) {
        return getSpecificUserRole(userId).equals(userRole);
    }

    private UserRole getCurrentUserRole() {
        User currentUser = authenticationService.getAuthenticatedUser();
        return currentUser.getUserRole();
    }

    private UserRole getSpecificUserRole(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> BusinessExceptions.ACCOUNT_MISSING);
        return user.getUserRole();
    }
}
