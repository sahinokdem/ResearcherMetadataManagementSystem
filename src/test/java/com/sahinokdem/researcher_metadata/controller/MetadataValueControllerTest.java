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
    @Autowired
    private MetadataValueRepository metadataValueRepository;

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
                ), "All values should have IDs, names and types and owned by researcher")
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
    public void given_AdminUser_when_GetAllValuesWithType_then_Forbidden() {
        //GIVEN
        String registryName = "name";
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/type?registryName={registryName}", HttpMethod.GET, request,
                new ParameterizedTypeReference<ErrorDto>() {}, registryName);
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_GetAllValuesWithType_then_Forbidden() {
        //GIVEN
        String registryName = "name";
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String hrSpecialistToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hrSpecialistToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/type?registryName={registryName}", HttpMethod.GET, request,
                new ParameterizedTypeReference<ErrorDto>() {}, registryName);
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_EditorUser_when_GetSpecificValue_then_StatusOk_and_ValueReturned() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
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
        assertAll(
                "Editor user should be able to get specific value details",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK"),
                () -> assertNotNull(response.getBody(), "Value response should not be null"),
                () -> assertEquals(valueId, response.getBody().getId(), "Retrieved value ID should match the requested ID")
        );
    }

    @Test
    public void given_ResearcherUser_when_GetSpecificValue_then_StatusOk_and_ValueReturned() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
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
        assertAll(
                "Researcher user should be able to get their own specific value details",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK"),
                () -> assertNotNull(response.getBody(), "Value response should not be null"),
                () -> assertEquals(valueId, response.getBody().getId(), "Retrieved value ID should match the requested ID"),
                () -> assertEquals(user.getId(), response.getBody().getUserId(), "Retrieved value should belong to the researcher")
        );
    }

    @Test
    public void given_ResearcherUser_when_GetSpecificValue_ofAnotherUser_then_Forbidden() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(1).getId(); //Another researchers metadata value id
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
    public void given_AdminUser_when_GetSpecificValue_then_Forbidden() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.GET, request, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_GetSpecificValue_then_Forbidden() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String hrSpecialistToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hrSpecialistToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.GET, request, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
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
        assertAll(
                "Editor user should be able to add a new value",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 Ok"),
                () -> assertNotNull(response.getBody(), "Value response should not be null"),
                () -> assertEquals(userId, response.getBody().getUserId(), "Created value's userId should match the request"),
                () -> assertEquals(registryId, response.getBody().getRegistryId(), "Created value's registryId should match the request"),
                () -> assertEquals(value, response.getBody().getValue(), "Created value should match the request")
        );
    }

    @Test
    public void given_AdminUser_when_AddValue_then_Forbidden() {
        // GIVEN
        String userId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a";
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        String value = "new_value";
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided"));
        String adminToken = tokenService.getTokenFor(user);
        MetadataValueCreateRequest request = new MetadataValueCreateRequest(userId, registryId, value);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataValueCreateRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/add", HttpMethod.POST, requestEntity, ErrorDto.class);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_AddValue_then_Forbidden() {
        // GIVEN
        String userId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a";
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        String value = "new_value";
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String hrSpecialistToken = tokenService.getTokenFor(user);
        MetadataValueCreateRequest request = new MetadataValueCreateRequest(userId, registryId, value);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hrSpecialistToken);
        HttpEntity<MetadataValueCreateRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/add", HttpMethod.POST, requestEntity, ErrorDto.class);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_AddValue_then_Forbidden() {
        // GIVEN
        String userId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b8a";
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        String value = "new_value";
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String researcherToken = tokenService.getTokenFor(user);
        MetadataValueCreateRequest request = new MetadataValueCreateRequest(userId, registryId, value);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<MetadataValueCreateRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/add", HttpMethod.POST, requestEntity, ErrorDto.class);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_EditorUser_when_UpdateExistingValue_then_StatusOk_and_UpdatedValueReturned() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
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
        assertAll(
                "Editor user should be able to update an existing value",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK"),
                () -> assertNotNull(response.getBody(), "Value response should not be null"),
                () -> assertEquals(valueId, response.getBody().getId(), "Updated value ID should match the requested ID"),
                () -> assertEquals(updatedValue, response.getBody().getValue(), "Updated value should match the request")
        );
    }

    @Test
    public void given_AdminUser_when_UpdateExistingValue_then_Forbidden() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
        String updatedValue = "updated_value_by_admin";
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataValueUpdateRequest request = new MetadataValueUpdateRequest(updatedValue);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataValueUpdateRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.PUT, requestEntity, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_UpdateExistingValue_then_Forbidden() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
        String updatedValue = "updated_value_by_hr";
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided"));
        String hrSpecialistToken = tokenService.getTokenFor(user);
        MetadataValueUpdateRequest request = new MetadataValueUpdateRequest(updatedValue);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hrSpecialistToken);
        HttpEntity<MetadataValueUpdateRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.PUT, requestEntity, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_UpdateExistingValue_then_Forbidden() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
        String updatedValue = "updated_value_by_researcher";
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided"));
        String researcherToken = tokenService.getTokenFor(user);
        MetadataValueUpdateRequest request = new MetadataValueUpdateRequest(updatedValue);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<MetadataValueUpdateRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.PUT, requestEntity, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_EditorUser_when_DeleteExistingValue_then_StatusOk() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
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
    public void given_AdminUser_when_DeleteExistingValue_then_Forbidden() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided"));
        String adminToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.DELETE, requestEntity, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_DeleteExistingValue_then_Forbidden() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided"));
        String hrSpecialistToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hrSpecialistToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.DELETE, requestEntity, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_DeleteExistingValue_then_Forbidden() {
        // GIVEN
        String valueId = metadataValueRepository.findAll().get(0).getId();
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided"));
        String researcherToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-value/{id}", HttpMethod.DELETE, requestEntity, ErrorDto.class, valueId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }
}
