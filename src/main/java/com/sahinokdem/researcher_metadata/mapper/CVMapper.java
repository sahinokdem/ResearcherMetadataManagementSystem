package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.CVInfo;
import com.sahinokdem.researcher_metadata.entity.FileInfo;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.Result;
import com.sahinokdem.researcher_metadata.model.request.CVRequest;
import com.sahinokdem.researcher_metadata.model.request.ReviewRequest;
import com.sahinokdem.researcher_metadata.model.response.CVResponse;
import org.springframework.stereotype.Service;

@Service
public class CVMapper {

    public CVInfo toEntity(User owner, FileInfo fileInfo, CVRequest request) {
        CVInfo cvInfo = new CVInfo(
                fileInfo,
                request.getCvAssociation());
        cvInfo.setOwner(owner);
        cvInfo.setResult(Result.WAITING_FOR_ACCEPTANCE);
        cvInfo.setReason("Your cv associated, waiting for HR review");
        return cvInfo;
    }

    public CVInfo toEntity(CVInfo cvInfo, ReviewRequest request) {
        if (request.isAccepted()) cvInfo.setResult(Result.ACCEPTED);
        else cvInfo.setResult(Result.REJECTED);
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
}
