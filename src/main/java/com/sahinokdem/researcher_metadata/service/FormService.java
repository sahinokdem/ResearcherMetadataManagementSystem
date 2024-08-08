package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.Form;
import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.FormAndCvResult;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.mapper.FormMapper;
import com.sahinokdem.researcher_metadata.model.request.FormRequest;
import com.sahinokdem.researcher_metadata.model.request.ReviewRequest;
import com.sahinokdem.researcher_metadata.model.response.FormResponse;
import com.sahinokdem.researcher_metadata.repository.FormRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FormService {

    private final UserRoleService userRoleService;
    private final FormMapper formMapper;
    private final AuthenticationService authenticationService;
    private final EntityAccessService entityAccessService;
    private final FormRepository formRepository;

    public List<FormResponse> getAllForms() {
        userRoleService.assertCurrentUserRole(UserRole.HR_SPECIALIST);
        List<Form> forms = formRepository.findAll();
        return forms.stream()
                .map(formMapper::toResponse)
                .collect(Collectors.toList());
    }

    public FormResponse getForm(String formId) {
        Form form = entityAccessService.getEntity(formId, formRepository,
                UserRole.JOP_APPLICANT, UserRole.HR_SPECIALIST, BusinessExceptions.FORM_NOT_FOUND);
        return formMapper.toResponse(form);
    }

    public FormResponse sendForm(FormRequest formRequest) {
        userRoleService.assertCurrentUserRole(UserRole.JOP_APPLICANT);
        User user = authenticationService.getAuthenticatedUser();
        Form form = formMapper.toEntity(user, formRequest);
        formRepository.save(form);
        return formMapper.toResponse(form);
    }

    public FormResponse applyForm(String formId, ReviewRequest request) {
        userRoleService.assertCurrentUserRole(UserRole.HR_SPECIALIST);
        Form form = formRepository.findById(formId).orElseThrow(
                () -> BusinessExceptions.FORM_NOT_FOUND);
        Form reviewedForm = formMapper.toEntity(form, request);
        formRepository.save(reviewedForm);
        return formMapper.toResponse(reviewedForm);
    }
}
