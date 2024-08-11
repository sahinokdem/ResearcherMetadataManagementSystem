package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.exception.ErrorDto;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueCreateRequest;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueUpdateRequest;
import com.sahinokdem.researcher_metadata.util.TestUtils;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractMetadataValueControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
