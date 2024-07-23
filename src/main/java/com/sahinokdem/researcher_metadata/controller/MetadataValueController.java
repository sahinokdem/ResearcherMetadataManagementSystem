package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.request.MetadataValueRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.service.MetadataValueService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/metadata-value")
@AllArgsConstructor
public class MetadataValueController {

    private MetadataValueService metadataValueService;

    @PostMapping("/add")
    public MetadataValueResponse addMetadataValue(
            @Valid @RequestBody MetadataValueRequest metadataValueRequest) {
        return metadataValueService.addMetadataValue(metadataValueRequest);
    }
}
