package com.alpergayretoglu.spring_boot_template.service;


import com.alpergayretoglu.spring_boot_template.enums.UserRole;
import com.alpergayretoglu.spring_boot_template.model.request.LoginRequest;
import com.alpergayretoglu.spring_boot_template.model.response.LoginResponse;
import com.alpergayretoglu.spring_boot_template.model.request.RegisterRequest;
import com.alpergayretoglu.spring_boot_template.model.response.RegisterResponse;
import com.alpergayretoglu.spring_boot_template.entity.User;
import com.alpergayretoglu.spring_boot_template.repository.UserRepository;
import com.alpergayretoglu.spring_boot_template.exception.BusinessExceptions;
import com.alpergayretoglu.spring_boot_template.security.JwtService;
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
                jwtService.createToken(userEntity.getId())
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
                jwtService.createToken(user.getId())
        );
    }

    public Optional<User> getAuthenticatedUser() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.equals("anonymousUser")) {
            return Optional.empty();
        }

        return userRepository.findById(principal);
    }
}
