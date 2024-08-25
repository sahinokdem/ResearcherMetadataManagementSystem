package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.TokenService;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.exception.ErrorDto;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueCreateRequest;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueUpdateRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataRegistryResponse;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.MetadataValueRepository;
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
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetadataValueControllerTest extends AbstractControllerTest {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void given_EditorUser_when_GetAllValues_then_StatusOk_and_AllValuesReturned() {
        //GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        //WHEN
        ResponseEntity<List<MetadataValueResponse>> response = restTemplate.exchange(
                "/metadata-value", HttpMethod.GET, request,
                new ParameterizedTypeReference<List<MetadataValueResponse>>() {}
        );
        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<MetadataValueResponse> metadataValues = response.getBody();
        assertFalse(metadataValues.isEmpty(), "Value list should not be empty");
        assertTrue(metadataValues.stream().allMatch(eachValue ->
                eachValue.getId() != null && eachValue.getUserId() != null &&
                        eachValue.getRegistryId() != null && eachValue.getValue() != null
        ), "All values should have IDs, names and types");

    }

    @Test
    public void given_ResearcherUser_when_GetAllValues_then_StatusOk_and_AllValuesWhichResearcherHasReturned() {
        //GIVEN
        User user = getUserByEmail("researcher@test.com");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        //WHEN
        ResponseEntity<List<MetadataValueResponse>> response = restTemplate.exchange(
                "/metadata-value", HttpMethod.GET, request,
                new ParameterizedTypeReference<List<MetadataValueResponse>>() {}
        );
        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<MetadataValueResponse> metadataValues = response.getBody();
        assertFalse(metadataValues.isEmpty(), "Value list should not be empty");
        assertTrue(metadataValues.stream().allMatch(eachValue ->
                eachValue.getId() != null && eachValue.getUserId() != null &&
                        eachValue.getRegistryId() != null && eachValue.getValue() != null  &&
                        eachValue.getUserId().equals(user.getId())
        ), "All values should have IDs, names and types and owned by researcher");

    }

    @Test
    public void given_NotAuthorizedUsers_when_GetAllValues_then_Forbidden() {
        given_NotAuthorizedUser_when_GetAllValues_then_Forbidden(adminToken);
        given_NotAuthorizedUser_when_GetAllValues_then_Forbidden(hrSpecialistToken);
    }

    @Test
    public void given_EditorUser_when_GetAllValuesWithType_then_StatusOk_and_AllValuesReturned() {
        //GIVEN
        String registryName = "name";
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<MetadataValueResponse>> response = restTemplate.exchange(
                "/metadata-value/type?registryName={registryName}", HttpMethod.GET, request,
                new ParameterizedTypeReference<List<MetadataValueResponse>>() {}, registryName);
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
    public void given_ResearcherUser_when_GetAllValuesWithType_then_StatusOk_and_AllValuesReturned() {
        //GIVEN
        String registryName = "name";
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<MetadataValueResponse>> response = restTemplate.exchange(
                "/metadata-value/type?registryName={registryName}", HttpMethod.GET, request,
                new ParameterizedTypeReference<List<MetadataValueResponse>>() {}, registryName);
        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<MetadataValueResponse> metadataValues = response.getBody();
        assertAll(
                () -> assertFalse(metadataValues.isEmpty(), "Value list should not be empty"),
                () -> assertTrue(metadataValues.stream().allMatch(eachValue ->
                        eachValue.getId() != null && eachValue.getUserId() != null &&
                                eachValue.getRegistryId() != null && eachValue.getValue() != null &&
                                eachValue.getUserId().equals(user.getId())
                ), "All values should have IDs, names and types and owned by researcher")
        );
    }

    @Test
    public void given_NotAuthorizedUsers_when_GetAllValuesWithType_then_Forbidden() {
        given_NotAuthorizedUser_when_GetAllValuesWithType_then_Forbidden(adminToken, "name");
        given_NotAuthorizedUser_when_GetAllValuesWithType_then_Forbidden(adminToken, "citation_count");
        given_NotAuthorizedUser_when_GetAllValuesWithType_then_Forbidden(hrSpecialistToken, "name");
        given_NotAuthorizedUser_when_GetAllValuesWithType_then_Forbidden(hrSpecialistToken, "citation_count");
    }

    @Test
    public void given_EditorUser_when_GetSpecificValue_then_StatusOk_and_ValueReturned() {
        // GIVEN
        String valueId = "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a";
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<MetadataValueResponse> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.GET, request, MetadataValueResponse.class, valueId);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK");
        assertNotNull(response.getBody(), "Value response should not be null");
        assertEquals(valueId, response.getBody().getId(), "Retrieved value ID should match the requested ID");
    }

    @Test
    public void given_ResearcherUser_when_GetSpecificValue_then_StatusOk_and_ValueReturned() {
        // GIVEN
        String valueId = "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a";
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String researcherToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<MetadataValueResponse> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.GET, request, MetadataValueResponse.class, valueId);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK");
        assertNotNull(response.getBody(), "Value response should not be null");
        assertEquals(valueId, response.getBody().getId(), "Retrieved value ID should match the requested ID");
        assertEquals(user.getId(), response.getBody().getUserId(), "Retrieved value should belong to the researcher");
    }

    @Test
    public void given_ResearcherUser_when_GetSpecificValue_ofAnotherUser_then_Forbidden() {
        // GIVEN
        String valueId = "d9a2f3d2-7b8b-4b32-9101-dc431b6c5v4a"; //Another researchers metadata value id
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String researcherToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.GET, request, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.METADATA_VALUE_NOT_FOUND, response);
    }

    @Test
    public void given_NotAuthorizedUsers_when_GetSpecificValue_then_Forbidden() {
        given_NotAuthorizedUser_when_GetSpecificValue_then_Forbidden(adminToken,
                "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a");
        given_NotAuthorizedUser_when_GetSpecificValue_then_Forbidden(hrSpecialistToken,
                "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a");
    }

    @Test
    public void given_EditorUser_when_AddValue_then_StatusOk_and_ValueReturned() {
        // GIVEN
        String userId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a";
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        String value = "new_value";
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided"));
        String editorToken = tokenService.getTokenFor(user);
        MetadataValueCreateRequest request = new MetadataValueCreateRequest(userId, registryId, value);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<MetadataValueCreateRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<MetadataValueResponse> response = restTemplate.exchange(
                "/metadata-value/add", HttpMethod.POST, requestEntity, MetadataValueResponse.class);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 Ok");
        assertNotNull(response.getBody(), "Value response should not be null");
        assertEquals(userId, response.getBody().getUserId(), "Created value's userId should match the request");
        assertEquals(registryId, response.getBody().getRegistryId(), "Created value's registryId should match the request");
        assertEquals(value, response.getBody().getValue(), "Created value should match the request");
    }

    @Test
    public void given_NotAuthorizedUsers_when_AddValue_then_Forbidden() {
        given_NotAuthorizedUser_when_AddValue_then_Forbidden(adminToken, "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a",
                "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a", "new_value");
        given_NotAuthorizedUser_when_AddValue_then_Forbidden(hrSpecialistToken, "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a",
                "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a", "new_value");
        given_NotAuthorizedUser_when_AddValue_then_Forbidden(researcherToken, "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a",
                "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a", "new_value");
    }

    @Test
    public void given_EditorUser_when_UpdateExistingValue_then_StatusOk_and_UpdatedValueReturned() {
        // GIVEN
        String valueId = "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a";
        String updatedValue = "updated_value";
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided"));
        String editorToken = tokenService.getTokenFor(user);
        MetadataValueUpdateRequest request = new MetadataValueUpdateRequest(updatedValue);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<MetadataValueUpdateRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<MetadataValueResponse> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.PUT, requestEntity, MetadataValueResponse.class, valueId);
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK");
        assertNotNull(response.getBody(), "Value response should not be null");
        assertEquals(valueId, response.getBody().getId(), "Updated value ID should match the requested ID");
        assertEquals(updatedValue, response.getBody().getValue(), "Updated value should match the request");
    }

    @Test
    public void given_NotAuthorizedUsers_when_UpdateExistingValue_then_Forbidden() {
        given_NotAuthorizedUser_when_UpdateExistingValue_then_Forbidden(adminToken,
                "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a", "updated_value");
        given_NotAuthorizedUser_when_UpdateExistingValue_then_Forbidden(hrSpecialistToken,
                "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a", "updated_value");
        given_NotAuthorizedUser_when_UpdateExistingValue_then_Forbidden(researcherToken,
                "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a", "updated_value");
    }

    @Test
    public void given_EditorUser_when_DeleteExistingValue_then_StatusOk() {
        // GIVEN
        String valueId = "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a";
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided"));
        String editorToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.DELETE, requestEntity, Void.class, valueId);
        // THEN
        assertAll(
                "Editor user should be able to delete an existing value",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 Ok")
        );
    }

    @Test
    public void given_NotAuthorizedUser_when_DeleteExistingValue_then_Forbidden() {
        given_NotAuthorizedUser_when_DeleteExistingValue_then_Forbidden(adminToken, "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a");
        given_NotAuthorizedUser_when_DeleteExistingValue_then_Forbidden(hrSpecialistToken, "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a");
        given_NotAuthorizedUser_when_DeleteExistingValue_then_Forbidden(researcherToken, "d9a2f3d2-7b8b-4b32-9101-dc431b6c5b8a");
    }

    public void given_NotAuthorizedUser_when_GetAllValues_then_Forbidden(String userToken) {
        //GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        //WHEN
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value", HttpMethod.GET, request, new ParameterizedTypeReference<ErrorDto>() {}
        );
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    public void given_NotAuthorizedUser_when_GetAllValuesWithType_then_Forbidden(String userToken, String registryName) {
        //GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        //WHEN
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/type?registryName={registryName}", HttpMethod.GET, request,
                new ParameterizedTypeReference<ErrorDto>() {}, registryName);
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    public void given_NotAuthorizedUser_when_GetSpecificValue_then_Forbidden(String userToken, String valueId) {
        // GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        // WHEN
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.GET, request, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    public void given_NotAuthorizedUser_when_AddValue_then_Forbidden(String userToken, String userId, String registryId, String value) {
        // GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userToken);
        MetadataValueCreateRequest request = new MetadataValueCreateRequest(userId, registryId, value);
        // WHEN
        HttpEntity<MetadataValueCreateRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/add", HttpMethod.POST, requestEntity, ErrorDto.class);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    public void given_NotAuthorizedUser_when_UpdateExistingValue_then_Forbidden(String userToken, String valueId, String updatedValue) {
        // GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userToken);
        MetadataValueUpdateRequest request = new MetadataValueUpdateRequest(updatedValue);
        // WHEN
        HttpEntity<MetadataValueUpdateRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.PUT, requestEntity, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    public void given_NotAuthorizedUser_when_DeleteExistingValue_then_Forbidden(String userToken, String valueId) {
        // GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        // WHEN
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.DELETE, requestEntity, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }
}
