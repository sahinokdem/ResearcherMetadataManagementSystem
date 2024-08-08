package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.Form;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.FormAndCvResult;
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
        return new Form(
                request.getNameAndSurname(),
                request.getEmail(),
                request.getDateOfBirth(),
                request.getExternalApiId(),
                user,
                FormAndCvResult.WAITING_FOR_ACCEPTANCE,
                "Your form sent, waiting for HR review"
        );
    }

    public Form toEntity(Form form, ReviewRequest request) {
        if (request.isAccepted()) form.setResult(FormAndCvResult.ACCEPTED);
        else form.setResult(FormAndCvResult.REJECTED);
        form.setReason(request.getReason());
        return form;
    }
}
