package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.request.MetadataValueRequest;
import com.sahinokdem.researcher_metadata.model.response.MetadataValueResponse;
import com.sahinokdem.researcher_metadata.service.MetadataValueService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PutMapping("/{metadataValueId}")
    public MetadataValueResponse updateMetadataValue(
            @PathVariable String metadataValueId
            , @Valid @RequestBody MetadataValueRequest metadataValueRequest) {
        return metadataValueService.updateMetadataValue(metadataValueId, metadataValueRequest);
    }

    @DeleteMapping("/{metadataValueId}")
    public void deleteMetadataValue(@PathVariable String metadataValueId) {
        metadataValueService.deleteMetadataValue(metadataValueId);
    }

    @GetMapping("/{metadataValueId}")
    public MetadataValueResponse getMetadataValue(@PathVariable String metadataValueId) {
        return metadataValueService.getMetadataValue(metadataValueId);
    }

    @GetMapping
    public List<MetadataValueResponse> getAllMetadataValues() {
        return metadataValueService.getAllMetadataValues();
    }

    @GetMapping("/type")
    public List<MetadataValueResponse> getAllMetadataValuesByRegistry(@RequestParam String registryName) {
        return metadataValueService.getMetadataValuesByRegistry(registryName);
    }
}
