package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.TokenService;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.model.response.MetadataRegistryResponse;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data.sql")
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
        List<MetadataValueResponse> metadataRegistries = response.getBody();
        assertAll(
                () -> assertFalse(metadataRegistries.isEmpty(), "Value list should not be empty"),
                () -> assertTrue(metadataRegistries.stream().allMatch(eachValue ->
                        eachValue.getId() != null && eachValue.getUserId() != null &&
                                eachValue.getRegistryId() != null && eachValue.getValue() != null
                ), "All values should have IDs, names and types")
        );
    }
}
