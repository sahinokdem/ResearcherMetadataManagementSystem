package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.Form;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.FormAndCvResult;
import com.sahinokdem.researcher_metadata.model.request.FormRequest;
import com.sahinokdem.researcher_metadata.model.response.FormResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FormMapper {

    public FormResponse toResponse(Form form) {
        return new FormResponse(
                form.getId(),
                form.getResult()
        );
    }

    public Form toEntity(User user, FormRequest request) {
        return new Form(
                request.getNameAndSurname(),
                request.getEmail(),
                request.getDateOfBirth(),
                request.getExternalApiId(),
                user,
                FormAndCvResult.WAITING_FOR_ACCEPTANCE
        );
    }
}
