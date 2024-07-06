package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.request.LoginRequest;
import com.sahinokdem.researcher_metadata.model.request.RegisterRequest;
import com.sahinokdem.researcher_metadata.model.response.LoginResponse;
import com.sahinokdem.researcher_metadata.model.response.RegisterResponse;
import com.sahinokdem.researcher_metadata.service.AuthenticationService;
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
