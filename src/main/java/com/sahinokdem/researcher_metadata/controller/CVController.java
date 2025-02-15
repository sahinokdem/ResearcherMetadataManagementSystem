package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.request.CVRequest;
import com.sahinokdem.researcher_metadata.model.request.ReviewRequest;
import com.sahinokdem.researcher_metadata.model.response.CVResponse;
import com.sahinokdem.researcher_metadata.service.CVService;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cv")
@AllArgsConstructor
public class CVController {

    private final CVService cvService;

    @GetMapping
    public List<CVResponse> getAllCVInfos(@ParameterObject Pageable pageable) {
        return cvService.getAllCVInfos(pageable);
    }

    @GetMapping("{cvId}")
    public CVResponse getCVInfo(@PathVariable String cvId) {
        return cvService.getCVInfo(cvId);
    }

    @PostMapping("/{fileId}/associate")
    public CVResponse associateCVFile(@PathVariable String fileId, @RequestBody CVRequest cvRequest) {
        return cvService.associateCVFile(fileId, cvRequest);
    }

    @PutMapping("/{cvId}/review")
    public CVResponse reviewCV(@PathVariable String cvId, @RequestBody ReviewRequest cvRequest) {
        return cvService.reviewCV(cvId, cvRequest);
    }
}
