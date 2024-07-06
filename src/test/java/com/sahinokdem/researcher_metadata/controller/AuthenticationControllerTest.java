package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.model.request.LoginRequest;
import com.sahinokdem.researcher_metadata.model.request.RegisterRequest;
import com.sahinokdem.researcher_metadata.model.response.LoginResponse;
import com.sahinokdem.researcher_metadata.model.response.RegisterResponse;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //LOGIN TESTS

    @Test
    public void given_ValidLoginRequest_when_Login_then_StatusCodeIs200_and_TokenNotNull() {
        // Given
        String email = "hr_specialist@test.com";
        String password = "123456";

        LoginRequest loginRequest = new LoginRequest(email, password);

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );

        // When
        JsonNode requestBodyJson = objectMapper.valueToTree(loginRequest);

        final HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, new HttpHeaders());
        final ResponseEntity<LoginResponse> response = restTemplate.exchange(
                "/auth/login", HttpMethod.POST,
                request, new ParameterizedTypeReference<LoginResponse>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        LoginResponse loginResponse = response.getBody();
        assertNotNull(loginResponse);

        assertEquals(user.getId(), loginResponse.getId());
        assertNotNull(loginResponse.getToken());
    }

    @Test
    public void given_InvalidLoginRequest_with_NonexistentEmail_when_Login_then_StatusCodeIs422() {
        // Given
        String email = "nonexistent@test.com";
        String password = "123456";

        LoginRequest loginRequest = new LoginRequest(email, password);

        // When
        JsonNode requestBodyJson = objectMapper.valueToTree(loginRequest);

        final HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, new HttpHeaders());
        final ResponseEntity<String> response = restTemplate.exchange(
                "/auth/login", HttpMethod.POST,
                request, new ParameterizedTypeReference<String>() {}
        );

        // Then
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void given_InvalidLoginRequest_with_InvalidEmail_when_Login_then_StatusCodeIs400() {
        // Given
        String email = "invalidEmail";
        String password = "invalidPassword";

        LoginRequest loginRequest = new LoginRequest(email, password);

        // When
        JsonNode requestBodyJson = objectMapper.valueToTree(loginRequest);

        final HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, new HttpHeaders());
        final ResponseEntity<String> response = restTemplate.exchange(
                "/auth/login", HttpMethod.POST,
                request, new ParameterizedTypeReference<String>() {}
        );

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void given_InvalidLoginRequest_with_InvalidPassword_when_Login_then_StatusCodeIs422() {
        // Given
        String email = "hr_specialist@test.com";
        String password = "invalidPassword";

        LoginRequest loginRequest = new LoginRequest(email, password);

        // When
        JsonNode requestBodyJson = objectMapper.valueToTree(loginRequest);

        final HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, new HttpHeaders());
        final ResponseEntity<String> response = restTemplate.exchange(
                "/auth/login", HttpMethod.POST,
                request, new ParameterizedTypeReference<String>() {}
        );

        // Then
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    //REGISTER TESTS

    @Test
    public void given_ValidRegisterRequest_when_Register_then_StatusCodeIs200_and_TokenNotNull() {
        // Given
        String email = "newuser@test.com";
        String password = "123456";
        RegisterRequest registerRequest = new RegisterRequest(email, password);

        // When
        JsonNode requestBodyJson = objectMapper.valueToTree(registerRequest);
        HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, new HttpHeaders());
        ResponseEntity<RegisterResponse> response = restTemplate.exchange(
                "/auth/register", HttpMethod.POST, request, new ParameterizedTypeReference<RegisterResponse>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegisterResponse registerResponse = response.getBody();
        assertNotNull(registerResponse);
        assertNotNull(registerResponse.getToken());
    }

    @Test
    public void given_InvalidRegisterRequest_with_InvalidEmail_when_Register_then_StatusCodeIs400() {
        // Given
        String email = "invalidEmail";
        String password = "123456";
        RegisterRequest registerRequest = new RegisterRequest(email, password);

        // When
        JsonNode requestBodyJson = objectMapper.valueToTree(registerRequest);
        HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                "/auth/register", HttpMethod.POST, request, String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void given_InvalidRegisterRequest_with_ExistentEmail_when_Register_then_StatusCodeIs409() {
        // Given
        String existingEmail = "hr_specialist@test.com";
        String password = "123456";
        RegisterRequest registerRequest = new RegisterRequest(existingEmail, password);

        // When
        JsonNode requestBodyJson = objectMapper.valueToTree(registerRequest);
        HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                "/auth/register", HttpMethod.POST, request, String.class
        );

        // Then (expecting a conflict due to the duplicate email)
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}