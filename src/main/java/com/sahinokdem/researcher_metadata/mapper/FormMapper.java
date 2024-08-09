package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.Form;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.Result;
import com.sahinokdem.researcher_metadata.model.request.FormRequest;
import com.sahinokdem.researcher_metadata.model.request.ReviewRequest;
import com.sahinokdem.researcher_metadata.model.response.FormResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FormMapper {

    public FormResponse toResponse(Form form) {
        return new FormResponse(
                form.getId(),
                form.getResult(),
                form.getReason()
        );
    }

    public Form toEntity(User user, FormRequest request) {
        Form form = new Form(
                request.getNameAndSurname(),
                request.getEmail(),
                request.getDateOfBirth(),
                request.getExternalApiId());
        form.setOwner(user);
        form.setResult(Result.WAITING_FOR_ACCEPTANCE);
        form.setReason("Your form sent, waiting for HR review");
        return form;
    }

    public Form toEntity(Form form, ReviewRequest request) {
        if (request.isAccepted()) form.setResult(Result.ACCEPTED);
        else form.setResult(Result.REJECTED);
        form.setReason(request.getReason());
        return form;
    }
}
