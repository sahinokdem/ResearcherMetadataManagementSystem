package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.model.response.UserResponse;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public UserResponse getUser(String userId) {
        if (!isCurrentUserAdmin()) throw BusinessExceptions.AUTHORIZATION_MISSING;
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> BusinessExceptions.USER_NOT_FOUND
                );
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUserRole()
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
                currentUser.getEmail(),
                currentUser.getUserRole()
        );
    }

    public List<UserResponse> getUsers() {
        if (!isCurrentUserAdmin()) throw BusinessExceptions.AUTHORIZATION_MISSING;
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(
                    new UserResponse(
                            user.getId(),
                            user.getEmail(),
                            user.getUserRole()
                            ));
        }
        return userResponses;
    }

    private boolean isCurrentUserAdmin() {
        User currentUser = authenticationService
                .getAuthenticatedUser()
                .orElseThrow(
                        () -> BusinessExceptions.ACCOUNT_MISSING
                );
        return currentUser.getUserRole().equals(UserRole.ADMIN);
    }
}
