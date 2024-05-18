package com.alpergayretoglu.spring_boot_template.controller;

import com.alpergayretoglu.spring_boot_template.model.request.LoginRequest;
import com.alpergayretoglu.spring_boot_template.model.request.RegisterRequest;
import com.alpergayretoglu.spring_boot_template.model.response.LoginResponse;
import com.alpergayretoglu.spring_boot_template.model.response.RegisterResponse;
import com.alpergayretoglu.spring_boot_template.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

}
