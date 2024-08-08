package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.request.CVRequest;
import com.sahinokdem.researcher_metadata.model.response.CVResponse;
import com.sahinokdem.researcher_metadata.service.CVService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cv")
@AllArgsConstructor
public class CVController {

    private final CVService cvService;

    @GetMapping
    public List<CVResponse> getAllCVInfos() {
        return cvService.getAllCVInfos();
    }

    @GetMapping("{cvId}")
    public CVResponse getCVInfo(@PathVariable String cvId) {
        return cvService.getCVInfo(cvId);
    }

    @PostMapping("/{fileId}/associate")
    public CVResponse associateCVFile(@PathVariable String fileId, @RequestBody CVRequest cvRequest) {
        return cvService.associateCVFile(fileId, cvRequest);
    }
}
