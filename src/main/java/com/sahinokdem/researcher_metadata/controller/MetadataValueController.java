package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.request.MetadataValueCreateRequest;
import com.sahinokdem.researcher_metadata.model.request.MetadataValueUpdateRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.service.MetadataValueService;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/metadata-value")
@AllArgsConstructor
public class MetadataValueController {

    private MetadataValueService metadataValueService;

    @GetMapping
    public List<MetadataValueResponse> getAllMetadataValues(@ParameterObject Pageable pageable) {
        return metadataValueService.getAllMetadataValues(pageable);
    }

    @GetMapping("/type")
    public List<MetadataValueResponse> getAllMetadataValuesByRegistry(@RequestParam String registryName) {
        return metadataValueService.getMetadataValuesByRegistry(registryName);
    }

    @GetMapping("/{metadataValueId}")
    public MetadataValueResponse getMetadataValue(@PathVariable String metadataValueId) {
        return metadataValueService.getMetadataValue(metadataValueId);
    }

    @PostMapping("/add")
    public MetadataValueResponse addMetadataValue(
            @Valid @RequestBody MetadataValueCreateRequest metadataValueRequest) {
        return metadataValueService.addMetadataValue(metadataValueRequest);
    }

    @PutMapping("/{metadataValueId}")
    public MetadataValueResponse updateMetadataValue(
            @PathVariable String metadataValueId,
            @Valid @RequestBody MetadataValueUpdateRequest metadataValueRequest) {
        return metadataValueService.updateMetadataValue(metadataValueId, metadataValueRequest);
    }

    @DeleteMapping("/{metadataValueId}")
    public void deleteMetadataValue(@PathVariable String metadataValueId) {
        metadataValueService.deleteMetadataValue(metadataValueId);
    }
}
