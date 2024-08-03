package com.sahinokdem.researcher_metadata.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sahinokdem.researcher_metadata.TokenService;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.MetadataRegistryType;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.exception.ErrorDto;
import com.sahinokdem.researcher_metadata.model.request.MetadataRegistryRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataRegistryResponse;
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
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MetadataRegistryControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void given_EditorUser_when_GetAllRegistries_then_StatusOk_and_AllRegistriesReturned() {
        //GIVEN
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<MetadataRegistryResponse>> response = restTemplate.exchange(
                "/metadata-registry", HttpMethod.GET, request, new ParameterizedTypeReference<List<MetadataRegistryResponse>>() {}
        );
        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<MetadataRegistryResponse> metadataRegistries = response.getBody();
        assertAll(
                () -> assertFalse(metadataRegistries.isEmpty(), "Registry list should not be empty"),
                () -> assertTrue(metadataRegistries.stream().allMatch(eachRegistry ->
                        eachRegistry.getId() != null && eachRegistry.getName() != null &&
                                eachRegistry.getType() != null
                ), "All registries should have IDs, names and types")
        );
    }

    @Test
    public void given_AdminUser_when_GetAllRegistries_then_Forbidden() {
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
                "/metadata-registry", HttpMethod.GET, request, new ParameterizedTypeReference<ErrorDto>() {}
        );
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_GetAllRegistries_then_Forbidden() {
        //GIVEN
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String hr_specialistToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hr_specialistToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry", HttpMethod.GET, request, new ParameterizedTypeReference<ErrorDto>() {}
        );
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_GetAllRegistries_then_Forbidden() {
        //GIVEN
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String researcherToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry", HttpMethod.GET, request, new ParameterizedTypeReference<ErrorDto>() {}
        );
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_EditorUser_when_GetSpecificRegistry_then_StatusOk_and_RegistryReturned() {
        //GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<JsonNode> request = new HttpEntity<>(headers);
        ResponseEntity<MetadataRegistryResponse> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.GET, request, MetadataRegistryResponse.class, registryId);
        //THEN
        assertAll(
                "Editor user should be able to get specific registry details",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK"),
                () -> assertNotNull(response.getBody(), "Registry response should not be null"),
                () -> assertEquals(registryId, response.getBody().getId(), "Retrieved registry ID should match the requested ID")
        );
    }

    @Test
    public void given_AdminUser_when_GetSpecificRegistry_then_Forbidden() {
        //GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<JsonNode> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.GET, request, ErrorDto.class, registryId);
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_GetSpecificUser_then_Forbidden() {
        //GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String hr_specialistToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hr_specialistToken);
        HttpEntity<JsonNode> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.GET, request, ErrorDto.class, registryId);
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_GetSpecificUser_then_Forbidden() {
        //GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String researcherToken = tokenService.getTokenFor(user);
        //WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<JsonNode> request = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.GET, request, ErrorDto.class, registryId);
        //THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_EditorUser_when_AddNewRegistry_WithTypeString_then_StatusOk_and_RegistryReturned() {
        // GIVEN
        String registryName = "string_registry";
        MetadataRegistryType registryType = MetadataRegistryType.STRING;
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<MetadataRegistryResponse> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, MetadataRegistryResponse.class);
        // THEN
        assertAll(
                "Editor user should be able to create a new registry",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 Ok"),
                () -> assertNotNull(response.getBody(), "Registry response should not be null"),
                () -> assertEquals(registryName, response.getBody().getName(), "Created registry name should match the request"),
                () -> assertEquals(registryType, response.getBody().getType(), "Created registry type should match the request")
        );
    }

    @Test
    public void given_EditorUser_when_AddNewRegistry_WithTypePositiveInt_then_StatusOk_and_RegistryReturned() {
        // GIVEN
        String registryName = "positive_int_registry";
        MetadataRegistryType registryType = MetadataRegistryType.POSITIVE_INTEGER;
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<MetadataRegistryResponse> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, MetadataRegistryResponse.class);
        // THEN
        assertAll(
                "Editor user should be able to create a new registry",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 Ok"),
                () -> assertNotNull(response.getBody(), "Registry response should not be null"),
                () -> assertEquals(registryName, response.getBody().getName(), "Created registry name should match the request"),
                () -> assertEquals(registryType, response.getBody().getType(), "Created registry type should match the request")
        );
    }

    @Test
    public void given_EditorUser_when_AddNewRegistry_WithTypeEducationDegree_then_StatusOk_and_RegistryReturned() {
        // GIVEN
        String registryName = "education_degree_registry";
        MetadataRegistryType registryType = MetadataRegistryType.STRING;
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<MetadataRegistryResponse> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, MetadataRegistryResponse.class);
        // THEN
        assertAll(
                "Editor user should be able to create a new registry",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 Ok"),
                () -> assertNotNull(response.getBody(), "Registry response should not be null"),
                () -> assertEquals(registryName, response.getBody().getName(), "Created registry name should match the request"),
                () -> assertEquals(registryType, response.getBody().getType(), "Created registry type should match the request")
        );
    }

    @Test
    public void given_EditorUser_when_AddNewRegistry_WithTypeResearchField_then_StatusOk_and_RegistryReturned() {
        // GIVEN
        String registryName = "research_field_registry";
        MetadataRegistryType registryType = MetadataRegistryType.RESEARCH_FIELD;
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<MetadataRegistryResponse> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, MetadataRegistryResponse.class);
        // THEN
        assertAll(
                "Editor user should be able to create a new registry",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 Ok"),
                () -> assertNotNull(response.getBody(), "Registry response should not be null"),
                () -> assertEquals(registryName, response.getBody().getName(), "Created registry name should match the request"),
                () -> assertEquals(registryType, response.getBody().getType(), "Created registry type should match the request")
        );
    }

    @Test
    public void given_AdminUser_when_AddNewRegistry_WithTypeString_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_admin";
        MetadataRegistryType registryType = MetadataRegistryType.STRING;
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_AdminUser_when_AddNewRegistry_WithTypePositiveInt_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_admin";
        MetadataRegistryType registryType = MetadataRegistryType.POSITIVE_INTEGER;
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_AdminUser_when_AddNewRegistry_WithTypeEducationDegree_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_admin";
        MetadataRegistryType registryType = MetadataRegistryType.EDUCATION_DEGREE;
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_AdminUser_when_AddNewRegistry_WithTypeResearchField_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_admin";
        MetadataRegistryType registryType = MetadataRegistryType.RESEARCH_FIELD;
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_AddNewRegistry_WithTypeString_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_hr_specialist";
        MetadataRegistryType registryType = MetadataRegistryType.STRING;
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_AddNewRegistry_WithTypePositiveInt_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_hr_specialist";
        MetadataRegistryType registryType = MetadataRegistryType.POSITIVE_INTEGER;
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_AddNewRegistry_WithTypeEducationDegree_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_hr_specialist";
        MetadataRegistryType registryType = MetadataRegistryType.EDUCATION_DEGREE;
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_AddNewRegistry_WithTypeResearchField_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_hr_specialist";
        MetadataRegistryType registryType = MetadataRegistryType.RESEARCH_FIELD;
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_AddNewRegistry_WithTypeString_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_researcher";
        MetadataRegistryType registryType = MetadataRegistryType.STRING;
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_AddNewRegistry_WithTypePositiveInt_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_researcher";
        MetadataRegistryType registryType = MetadataRegistryType.POSITIVE_INTEGER;
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_AddNewRegistry_WithTypeEducationDegree_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_researcher";
        MetadataRegistryType registryType = MetadataRegistryType.EDUCATION_DEGREE;
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_AddNewRegistry_WithTypeResearchField_then_Forbidden() {
        // GIVEN
        String registryName = "new_registry_by_researcher";
        MetadataRegistryType registryType = MetadataRegistryType.RESEARCH_FIELD;
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                registryName,
                registryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/add", HttpMethod.POST, requestEntity, ErrorDto.class);

        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_EditorUser_when_UpdateExistingRegistry_then_StatusOk_and_UpdatedRegistryReturned() {
        // GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        String updatedRegistryName = "updated_registry";
        MetadataRegistryType updatedRegistryType = MetadataRegistryType.POSITIVE_INTEGER;
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);

        MetadataRegistryRequest request = new MetadataRegistryRequest(
                updatedRegistryName,
                updatedRegistryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<MetadataRegistryResponse> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.PUT, requestEntity, MetadataRegistryResponse.class, registryId);
        // THEN
        assertAll(
                "Editor user should be able to update an existing registry",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK"),
                () -> assertNotNull(response.getBody(), "Registry response should not be null"),
                () -> assertEquals(registryId, response.getBody().getId(), "Updated registry ID should match the requested ID"),
                () -> assertEquals(updatedRegistryName, response.getBody().getName(), "Updated registry name should match the request"),
                () -> assertEquals(updatedRegistryType, response.getBody().getType(), "Updated registry type should match the request")
        );
    }

    @Test
    public void given_AdminUser_when_UpdateExistingRegistry_then_Forbidden() {
        // GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        String updatedRegistryName = "updated_registry_by_admin";
        MetadataRegistryType updatedRegistryType = MetadataRegistryType.POSITIVE_INTEGER;
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                updatedRegistryName,
                updatedRegistryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.PUT, requestEntity, ErrorDto.class, registryId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_UpdateExistingRegistry_then_Forbidden() {
        // GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        String updatedRegistryName = "updated_registry_by_hr";
        MetadataRegistryType updatedRegistryType = MetadataRegistryType.POSITIVE_INTEGER;
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String hrSpecialistToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                updatedRegistryName,
                updatedRegistryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hrSpecialistToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.PUT, requestEntity, ErrorDto.class, registryId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_UpdateExistingRegistry_then_Forbidden() {
        // GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        String updatedRegistryName = "updated_registry_by_researcher";
        MetadataRegistryType updatedRegistryType = MetadataRegistryType.POSITIVE_INTEGER;
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String researcherToken = tokenService.getTokenFor(user);
        MetadataRegistryRequest request = new MetadataRegistryRequest(
                updatedRegistryName,
                updatedRegistryType.toString()
        );
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<MetadataRegistryRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.PUT, requestEntity, ErrorDto.class, registryId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    @DirtiesContext
    public void given_EditorUser_when_DeleteExistingRegistry_then_StatusOk() {
        // GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        User user = userRepository.findByEmail("editor@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String editorToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + editorToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.DELETE, requestEntity, Void.class, registryId);
        // THEN
        assertAll(
                "Editor user should be able to delete an existing registry",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 Ok")
        );
    }

    @Test
    public void given_AdminUser_when_DeleteExistingRegistry_then_Forbidden() {
        // GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        User user = userRepository.findByEmail("admin@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String adminToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.DELETE, requestEntity, ErrorDto.class, registryId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_HRSpecialistUser_when_DeleteExistingRegistry_then_Forbidden() {
        // GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        User user = userRepository.findByEmail("hr_specialist@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String hrSpecialistToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + hrSpecialistToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.DELETE, requestEntity, ErrorDto.class, registryId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }

    @Test
    public void given_ResearcherUser_when_DeleteExistingRegistry_then_Forbidden() {
        // GIVEN
        String registryId = "c9a2f3d2-7b8b-4b32-9101-dc223b6c5b4a";
        User user = userRepository.findByEmail("researcher@test.com").orElseThrow(
                () -> new RuntimeException("Invalid Test Data Provided")
        );
        String researcherToken = tokenService.getTokenFor(user);
        // WHEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + researcherToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                "/metadata-registry/{id}", HttpMethod.DELETE, requestEntity, ErrorDto.class, registryId);
        // THEN
        TestUtils.assertErrorDto(BusinessExceptions.AUTHORIZATION_MISSING, response);
    }
}
