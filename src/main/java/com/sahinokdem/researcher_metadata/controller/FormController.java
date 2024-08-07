package com.sahinokdem.researcher_metadata.controller;

import com.sahinokdem.researcher_metadata.model.request.FormRequest;
import com.sahinokdem.researcher_metadata.model.response.FormResponse;
import com.sahinokdem.researcher_metadata.service.FormService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/form")
@AllArgsConstructor
public class FormController {

    private final FormService formService;

    @PostMapping("/send")
    public FormResponse sendForm(@Valid @RequestBody FormRequest formRequest) {
        return formService.sendForm(formRequest);
    }
}
