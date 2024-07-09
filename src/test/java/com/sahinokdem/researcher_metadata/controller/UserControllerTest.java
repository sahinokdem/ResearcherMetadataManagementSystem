package com.sahinokdem.researcher_metadata.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahinokdem.researcher_metadata.TokenService;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.exception.BusinessException;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.exception.ErrorDto;
import com.sahinokdem.researcher_metadata.model.request.LoginRequest;
import com.sahinokdem.researcher_metadata.model.response.LoginResponse;
import com.sahinokdem.researcher_metadata.model.response.UserResponse;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    private String getAdminToken() {
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        return tokenService.getTokenFor(user);
    }

    private String getEditorToken() {
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        return tokenService.getTokenFor(user);
    }

    private String getHR_SpecialistToken() {
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        return tokenService.getTokenFor(user);
    }

    @Test
    public void given_AdminUser_when_GetAllUsers_then_StatusOk_and_AllUsersReturned() {
        //GIVEN
        String adminToken = getAdminToken();
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                "/user", HttpMethod.GET, request, new ParameterizedTypeReference<List<UserResponse>>() {}
        );
        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserResponse> users = response.getBody();
        assertAll(
                () -> assertFalse(users.isEmpty(), "User list should not be empty"),
                () -> assertTrue(users.stream().allMatch(user ->
                        user.getId() != null && user.getEmail() != null
                ), "All users should have IDs and emails")
        );
    }

    @Test
    public void given_AdminUser_when_GetSpecificUser_then_StatusOk_and_UserReturned() {
        //GIVEN
        String userId = "45358564-927d-447c-a832-c5c263ada7bc";
        String adminToken = getAdminToken();
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<JsonNode> request = new HttpEntity<>(headers);
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/user/{id}", HttpMethod.GET, request, UserResponse.class, userId);
        //THEN
        assertAll(
                "Admin user should be able to get specific user details",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK"),
                () -> assertNotNull(response.getBody(), "User response should not be null"),
                () -> assertEquals(userId, response.getBody().getId(), "Retrieved user ID should match the requested ID")
        );
    }

    @Test
    public void given_EditorUser_when_GetAllUsers_then_Forbidden() {
        //GIVEN
        String adminToken = getEditorToken();
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/user", HttpMethod.GET, request, new ParameterizedTypeReference<UserResponse>() {}
        );
        //THEN
        BusinessException expectedException = BusinessExceptions.AUTHORIZATION_MISSING;
        assertEquals(expectedException.getStatusCode(), response.getStatusCode().value());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals(expectedException.getMessage(), errorDto.getMessage());
    }

    @Test
    public void given_EditorUser_when_GetSpecificUser_then_Forbidden() {
        //GIVEN
        String userId = "45358564-927d-447c-a832-c5c263ada7bc";
        String adminToken = getEditorToken();
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<JsonNode> request = new HttpEntity<>(headers);
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/user/{id}", HttpMethod.GET, request, UserResponse.class, userId);
        //THEN
        BusinessException expectedException = BusinessExceptions.AUTHORIZATION_MISSING;
        assertEquals(expectedException.getStatusCode(), response.getStatusCode().value());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals(expectedException.getMessage(), errorDto.getMessage());
    }

    @Test
    public void given_HRSpecialistUser_when_GetAllUsers_then_Forbidden() {
        //GIVEN
        String adminToken = getHR_SpecialistToken();
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/user", HttpMethod.GET, request, new ParameterizedTypeReference<UserResponse>() {}
        );
        //THEN
        BusinessException expectedException = BusinessExceptions.AUTHORIZATION_MISSING;
        assertEquals(expectedException.getStatusCode(), response.getStatusCode().value());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals(expectedException.getMessage(), errorDto.getMessage());
    }

    @Test
    public void given_HRSpecialistUser_when_GetSpecificUser_then_Forbidden() {
        //GIVEN
        String userId = "45358564-927d-447c-a832-c5c263ada7bc";
        String adminToken = getHR_SpecialistToken();
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<JsonNode> request = new HttpEntity<>(headers);
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                "/user/{id}", HttpMethod.GET, request, UserResponse.class, userId);
        //THEN
        BusinessException expectedException = BusinessExceptions.AUTHORIZATION_MISSING;
        assertEquals(expectedException.getStatusCode(), response.getStatusCode().value());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals(expectedException.getMessage(), errorDto.getMessage());
    }
}
