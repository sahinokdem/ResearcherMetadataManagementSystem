package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.request.MetadataRegistryCreateRequest;
import com.sahinokdem.researcher_metadata.model.request.MetadataRegistryUpdateRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataRegistryResponse;
import com.sahinokdem.researcher_metadata.service.MetadataRegistryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/metadata-registry")
@AllArgsConstructor
public class MetadataRegistryController {

    private MetadataRegistryService metadataRegistryService;

    @PostMapping("/add")
    public MetadataRegistryResponse addMetadataRegistry(
            @Valid @RequestBody MetadataRegistryCreateRequest metadataRegistryCreateRequest) {
        return metadataRegistryService.addMetadataRegistry(metadataRegistryCreateRequest);
    }

    @PutMapping("/{metadataRegistryId}")
    public MetadataRegistryResponse updateMetadataRegistry(
            @PathVariable String metadataRegistryId,
            @Valid @RequestBody MetadataRegistryUpdateRequest metadataRegistryUpdateRequest) {
        return metadataRegistryService.updateMetadataRegistry(metadataRegistryId, metadataRegistryUpdateRequest);
    }

    @DeleteMapping("/{metadataRegistryId}")
    public void deleteMetadataRegistry(@PathVariable String metadataRegistryId) {
        metadataRegistryService.deleteMetadataRegistry(metadataRegistryId);
    }

    @GetMapping("/{metadataRegistryId}")
    public MetadataRegistryResponse getMetadataRegistry(@PathVariable String metadataRegistryId) {
        return metadataRegistryService.getMetadataRegistry(metadataRegistryId);
    }

    @GetMapping
    public List<MetadataRegistryResponse> getAllMetadataRegistries() {
        return metadataRegistryService.getAllMetadataRegistries();
    }
}
