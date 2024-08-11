package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.TokenService;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractControllerTest {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    public final String adminToken = tokenService.getTokenFor(getUserByEmail("admin@test.com"));
    public final String editorToken = tokenService.getTokenFor(getUserByEmail("editor@test.com"));
    public final String hrSpecialistToken = tokenService.getTokenFor(getUserByEmail("hr_specialist@test.com"));
    public final String researcherToken = tokenService.getTokenFor(getUserByEmail("researcher@test.com"));

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
    }
}
