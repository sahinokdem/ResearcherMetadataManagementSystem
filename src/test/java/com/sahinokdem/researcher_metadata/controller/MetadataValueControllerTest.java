package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.TokenService;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.exception.ErrorDto;
import com.sahinokdem.researcher_metadata.model.response.MetadataRegistryResponse;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import com.sahinokdem.researcher_metadata.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MetadataValueControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void given_EditorUser_when_GetAllValues_then_StatusOk_and_AllValuesReturned() {
        //GIVEN
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<MetadataValueResponse>> response = restTemplate.exchange(
                "/metadata-value", HttpMethod.GET, request, new ParameterizedTypeReference<List<MetadataValueResponse>>() {}
        );
        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<MetadataValueResponse> metadataValues = response.getBody();
        assertAll(
                () -> assertFalse(metadataValues.isEmpty(), "Value list should not be empty"),
                () -> assertTrue(metadataValues.stream().allMatch(eachValue ->
                        eachValue.getId() != null && eachValue.getUserId() != null &&
                                eachValue.getRegistryId() != null && eachValue.getValue() != null
                ), "All values should have IDs, names and types")
        );
    }

    @Test
    public void given_ResearcherUser_when_GetAllValues_then_StatusOk_and_AllValuesWhichResearcherHasReturned() {
        //GIVEN
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String researcherToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<MetadataValueResponse>> response = restTemplate.exchange(
                "/metadata-value", HttpMethod.GET, request, new ParameterizedTypeReference<List<MetadataValueResponse>>() {}
        );
        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<MetadataValueResponse> metadataValues = response.getBody();
        assertAll(
                () -> assertFalse(metadataValues.isEmpty(), "Value list should not be empty"),
                () -> assertTrue(metadataValues.stream().allMatch(eachValue ->
                        eachValue.getId() != null && eachValue.getUserId() != null &&
                                eachValue.getRegistryId() != null && eachValue.getValue() != null  &&
                                eachValue.getUserId().equals(user.getId())
                ), "All values should have IDs, names and types")
        );
    }

    @Test
    public void given_AdminUser_when_GetAllValues_then_Forbidden() {
        //GIVEN
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value", HttpMethod.GET, request, new ParameterizedTypeReference<ErrorDto>() {}
        );
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_GetAllValues_then_Forbidden() {
        //GIVEN
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String hrSpecialistToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hrSpecialistToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value", HttpMethod.GET, request, new ParameterizedTypeReference<ErrorDto>() {}
        );
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }
}
