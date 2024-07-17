package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.mapper.UserMapper;
import com.sahinokdem.researcher_metadata.model.response.UserResponse;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUser(String userId) {
        assertCurrentUserRole(UserRole.ADMIN);
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> BusinessExceptions.USER_NOT_FOUND
                );
        return userMapper.toResponse(user);
    }

    public UserResponse getCurrentUser() {
        User currentUser = authenticationService
                .getAuthenticatedUser()
                .orElseThrow(
                        () -> BusinessExceptions.ACCOUNT_MISSING
                );
        return userMapper.toResponse(currentUser);
    }

    public List<UserResponse> getUsers() {
        assertCurrentUserRole(UserRole.ADMIN);
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void assertCurrentUserRole(UserRole userRole) {
        User currentUser = authenticationService
                .getAuthenticatedUser()
                .orElseThrow(
                        () -> BusinessExceptions.ACCOUNT_MISSING
                );
        if (!currentUser.getUserRole().equals(userRole)) throw BusinessExceptions.AUTHORIZATION_MISSING;
    }
}
