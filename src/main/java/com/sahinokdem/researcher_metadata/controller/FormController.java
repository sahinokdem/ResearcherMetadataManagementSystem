package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.request.FormRequest;
import com.sahinokdem.researcher_metadata.model.response.FormResponse;
import com.sahinokdem.researcher_metadata.service.FormService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/form")
@AllArgsConstructor
public class FormController {

    private final FormService formService;

    @GetMapping
    public List<FormResponse> getAllForms() {
        return formService.getAllForms();
    }

    @GetMapping("{formId}")
    public FormResponse getForm(@PathVariable String formId) {
        return formService.getForm(formId);
    }

    @PostMapping("/send")
    public FormResponse sendForm(@Valid @RequestBody FormRequest formRequest) {
        return formService.sendForm(formRequest);
    }

    @PutMapping("{formId}/apply")
    public FormResponse applyForm(@PathVariable String formId) {
        return formService.applyForm(formId);
    }

    @PutMapping("{formId}/deny")
    public FormResponse denyForm(@PathVariable String formId) {
        return formService.denyForm(formId);
    }
}
