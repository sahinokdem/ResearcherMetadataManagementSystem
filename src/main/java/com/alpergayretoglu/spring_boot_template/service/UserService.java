package com.alpergayretoglu.spring_boot_template.service;

import com.alpergayretoglu.spring_boot_template.entity.User;
import com.alpergayretoglu.spring_boot_template.model.response.UserResponse;
import com.alpergayretoglu.spring_boot_template.repository.UserRepository;
import com.alpergayretoglu.spring_boot_template.exception.BusinessExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public UserResponse getUser(String userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> BusinessExceptions.USER_NOT_FOUND
                );

        return new UserResponse(
                user.getId(),
                user.getEmail()
        );
    }

    public UserResponse getCurrentUser() {
        User currentUser = authenticationService
                .getAuthenticatedUser()
                .orElseThrow(
                        () -> BusinessExceptions.ACCOUNT_MISSING
                );

        return new UserResponse(
                currentUser.getId(),
                currentUser.getEmail()
        );
    }
}
