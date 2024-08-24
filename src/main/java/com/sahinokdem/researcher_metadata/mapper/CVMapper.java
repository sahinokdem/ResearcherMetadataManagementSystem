package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.CVInfo;
import com.sahinokdem.researcher_metadata.entity.FileInfo;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.CVAssociation;
import com.sahinokdem.researcher_metadata.enums.Result;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.model.request.CVRequest;
import com.sahinokdem.researcher_metadata.model.request.ReviewRequest;
import com.sahinokdem.researcher_metadata.model.response.CVResponse;
import com.sahinokdem.researcher_metadata.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Locale;

@Service
@AllArgsConstructor
public class CVMapper {

    private final AuthenticationService authenticationService;

    public CVInfo toEntity(User owner, FileInfo fileInfo, CVRequest request) {
        CVInfo cvInfo = new CVInfo(
                fileInfo,
                toType(request.getCvAssociation()));
        cvInfo.setOwner(owner);
        cvInfo.setResult(Result.WAITING_FOR_ACCEPTANCE);
        cvInfo.setReason("Your cv associated, waiting for HR review");
        return cvInfo;
    }

    public CVInfo toEntity(CVInfo cvInfo, ReviewRequest request) {
        if (request.isAccepted()) {
            cvInfo.setResult(Result.ACCEPTED);
            User user = authenticationService.getAuthenticatedUser();
            user.setUserRole(UserRole.RESEARCHER);
        } else cvInfo.setResult(Result.REJECTED);
        cvInfo.setReason(request.getReason());
        return cvInfo;
    }

    public CVResponse toResponse(CVInfo cvInfo) {
        return new CVResponse(
                cvInfo.getId(),
                cvInfo.getFileInfo().getId(),
                cvInfo.getFileInfo().getName(),
                cvInfo.getCvAssociation(),
                cvInfo.getResult(),
                cvInfo.getReason()
        );
    }

    public CVAssociation toType(String type) {
        String normalizedType = type.replaceAll("\\s+", "_").toUpperCase(Locale.ENGLISH);
        try {
            return CVAssociation.valueOf(normalizedType);
        } catch (IllegalArgumentException e) {
            throw BusinessExceptions.INVALID_REGISTRY_TYPE;
        }
    }
}
