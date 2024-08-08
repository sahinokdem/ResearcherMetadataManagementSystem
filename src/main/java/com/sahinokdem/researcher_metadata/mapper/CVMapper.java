package com.sahinokdem.researcher_metadata.mapper;

import com.sahinokdem.researcher_metadata.entity.CVInfo;
import com.sahinokdem.researcher_metadata.entity.FileInfo;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.FormAndCvResult;
import com.sahinokdem.researcher_metadata.model.request.CVRequest;
import com.sahinokdem.researcher_metadata.model.response.CVResponse;
import org.springframework.stereotype.Service;

@Service
public class CVMapper {

    public CVInfo toEntity(User owner, FileInfo fileInfo, CVRequest request) {
        return new CVInfo(
                fileInfo,
                owner,
                request.getCvAssociation(),
                FormAndCvResult.WAITING_FOR_ACCEPTANCE
        );
    }

    public CVResponse toResponse(CVInfo cvInfo) {
        return new CVResponse(
                cvInfo.getId(),
                cvInfo.getFileInfo().getId(),
                cvInfo.getFileInfo().getName(),
                cvInfo.getCvAssociation(),
                cvInfo.getResult()
        );
    }
}
