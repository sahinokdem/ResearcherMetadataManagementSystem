package com.sahinokdem.researcher_metadata.util;

import com.sahinokdem.researcher_metadata.exception.BusinessException;
import com.sahinokdem.researcher_metadata.exception.ErrorDto;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestUtils {

    public static void assertErrorDto(BusinessException expectedException, ResponseEntity<ErrorDto> response) {
        assertEquals(expectedException.getStatusCode(), response.getStatusCode().value());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals(expectedException.getMessage(), errorDto.getMessage());
    }
}
