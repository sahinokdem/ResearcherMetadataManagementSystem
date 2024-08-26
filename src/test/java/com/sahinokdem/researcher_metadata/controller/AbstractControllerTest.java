package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.TokenService;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.PostConstruct;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractControllerTest {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    protected TestRestTemplate restTemplate;

    public String adminToken = null;
    public String editorToken = null;
    public String hrSpecialistToken = null;
    public String researcherToken = null;
    public String jobApplicantToken = null;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
    }

    @PostConstruct
    public void init() {
        adminToken = tokenService.getTokenFor(getUserByEmail("admin@test.com"));
        editorToken = tokenService.getTokenFor(getUserByEmail("editor@test.com"));
        hrSpecialistToken = tokenService.getTokenFor(getUserByEmail("hr_specialist@test.com"));
        researcherToken = tokenService.getTokenFor(getUserByEmail("researcher@test.com"));
        jobApplicantToken = tokenService.getTokenFor(getUserByEmail("job_applicant@test.com"));
    }
}
