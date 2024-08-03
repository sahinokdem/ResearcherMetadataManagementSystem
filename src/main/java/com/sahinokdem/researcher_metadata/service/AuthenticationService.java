package com.sahinokdem.researcher_metadata.service;


import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.model.request.LoginRequest;
import com.sahinokdem.researcher_metadata.model.response.LoginResponse;
import com.sahinokdem.researcher_metadata.model.request.RegisterRequest;
import com.sahinokdem.researcher_metadata.model.response.RegisterResponse;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest registerRequest) {
        userRepository
                .findByEmail(registerRequest.getEmail())
                .ifPresent(user -> {
                    throw BusinessExceptions.EMAIL_ALREADY_EXISTS;
                });

        User user = new User(
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                UserRole.JOP_APPLICANT
        );

        User userEntity = userRepository.save(user);

        return new RegisterResponse(
                userEntity.getId(),
                jwtService.createToken(userEntity.getId()),
                UserRole.JOP_APPLICANT
        );
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(
                        () -> BusinessExceptions.INVALID_CREDENTIALS
                );

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordEncoded())) {
            throw BusinessExceptions.INVALID_CREDENTIALS;
        }

        return new LoginResponse(
                user.getId(),
                jwtService.createToken(user.getId()),
                user.getUserRole()
        );
    }

    public Optional<User> getAuthenticatedUserOptional() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.equals("anonymousUser")) {
            return Optional.empty();
        }

        return userRepository.findById(principal);
    }

    public User getAuthenticatedUser() {
        return getAuthenticatedUserOptional()
                .orElseThrow(
                        () -> BusinessExceptions.ACCOUNT_MISSING
                );
    }
}
