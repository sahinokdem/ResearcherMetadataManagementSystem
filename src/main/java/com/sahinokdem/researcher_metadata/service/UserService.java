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
    private final UserRoleService userRoleService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUser(String userId) {
        userRoleService.assertCurrentUserRole(UserRole.ADMIN);
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> BusinessExceptions.USER_NOT_FOUND
                );
        return userMapper.toResponse(user);
    }

    public UserResponse getCurrentUser() {
        User currentUser = authenticationService.getAuthenticatedUser();
        return userMapper.toResponse(currentUser);
    }

    public List<UserResponse> getUsers() {
        userRoleService.assertCurrentUserRole(UserRole.ADMIN);
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
}
