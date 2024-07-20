package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.request.MetadataRegistryRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataRegistryResponse;
import com.sahinokdem.researcher_metadata.service.MetadataRegistryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/metadata-registry")
@AllArgsConstructor
public class MetadataRegistryController {

    private MetadataRegistryService metadataRegistryService;

    @PostMapping("/add")
    public MetadataRegistryResponse addMetadataRegistry(
            @Valid @RequestBody MetadataRegistryRequest metadataRegistryRequest) {
        return metadataRegistryService.addMetadataRegistry(metadataRegistryRequest);
    }
}
