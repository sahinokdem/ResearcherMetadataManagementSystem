package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.CVInfo;
import com.sahinokdem.researcher_metadata.entity.FileInfo;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.mapper.CVMapper;
import com.sahinokdem.researcher_metadata.model.request.CVRequest;
import com.sahinokdem.researcher_metadata.model.response.CVResponse;
import com.sahinokdem.researcher_metadata.repository.CVInfoRepository;
import com.sahinokdem.researcher_metadata.repository.FileInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CVService {

    private final FileService fileService;
    private final UserRoleService userRoleService;
    private final AuthenticationService authenticationService;
    private final EntityAccessService entityAccessService;
    private final CVMapper cvMapper;
    private final FileInfoRepository fileInfoRepository;
    private final CVInfoRepository cvInfoRepository;

    public List<CVResponse> getAllCVInfos() {
        userRoleService.assertCurrentUserRole(UserRole.HR_SPECIALIST);
        List<CVInfo> cvInfos = cvInfoRepository.findAll();
        return cvInfos.stream()
                .map(cvMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CVResponse getCVInfo(String cvId) {
        CVInfo cvInfo = entityAccessService.getEntity(cvId, cvInfoRepository, UserRole.JOP_APPLICANT,
                UserRole.HR_SPECIALIST, BusinessExceptions.CV_NOT_FOUND);
        return cvMapper.toResponse(cvInfo);
    }

    public CVResponse associateCVFile(String fileId, CVRequest cvRequest) {
        userRoleService.assertCurrentUserRole(UserRole.JOP_APPLICANT);
        User owner = authenticationService.getAuthenticatedUser();
        FileInfo fileInfo = fileInfoRepository.findById(fileId)
                .orElseThrow(() -> BusinessExceptions.FILE_NOT_FOUND);
        CVInfo cvInfo = cvMapper.toEntity(owner, fileInfo, cvRequest);
        cvInfoRepository.save(cvInfo);
        return cvMapper.toResponse(cvInfo);
    }
}
