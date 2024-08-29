package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.annotation.DFA;
import com.sahinokdem.researcher_metadata.config.PenaltyTimeConfig;
import com.sahinokdem.researcher_metadata.entity.*;
import com.sahinokdem.researcher_metadata.enums.Result;
import com.sahinokdem.researcher_metadata.enums.State;
import com.sahinokdem.researcher_metadata.exception.BusinessException;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.repository.CVInfoRepository;
import com.sahinokdem.researcher_metadata.repository.FormRepository;
import com.sahinokdem.researcher_metadata.repository.MetadataRegistryRepository;
import com.sahinokdem.researcher_metadata.repository.MetadataValueRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@DFA
@Service
public class JobApplicationService {

    private final PenaltyTimeConfig penaltyTimeConfig;
    private final FormRepository formRepository;
    private final CVInfoRepository cvInfoRepository;
    private final MetadataValueRepository metadataValueRepository;
    private final MetadataRegistryRepository metadataRegistryRepository;
    @Autowired
    private RestTemplate restTemplate;

    public JobApplicationService(PenaltyTimeConfig penaltyTimeConfig,
             FormRepository formRepository, CVInfoRepository cvInfoRepository,
             MetadataValueRepository metadataValueRepository, MetadataRegistryRepository metadataRegistryRepository) {
        this.penaltyTimeConfig = penaltyTimeConfig;
        this.formRepository = formRepository;
        this.cvInfoRepository = cvInfoRepository;
        this.metadataValueRepository = metadataValueRepository;
        this.metadataRegistryRepository = metadataRegistryRepository;
    }

    public String getCitationCountFromFastAPI(String researcherApiId) {
        String url = "http://localhost:8000/get_citation_count/" + researcherApiId;
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody().get("citation_count").toString();
            } else {
                throw BusinessExceptions.CITATION_COUNT_NOT_READ;
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            } else {
                throw BusinessExceptions.CITATION_COUNT_NOT_READ;
            }
        }
    }

    public void readyToFormSend(User user) {
        State userState = user.getCurrentState();
        Form previousForm = formRepository.findTopByOwnerOrderByCreatedDateDesc(user);
        CVInfo previousCvInfo = cvInfoRepository.findTopByOwnerOrderByCreatedDateDesc(user);
        if (previousForm == null) {
            return;
        }
        if (userState == State.SEND_FORM) {
            checkApplicationResult(previousForm, penaltyTimeConfig.getLongPenaltyTime(), State.SEND_FORM, user);
        } else if (userState == State.SECOND_CV_SENT) {
            checkApplicationResult(previousCvInfo, penaltyTimeConfig.getLongPenaltyTime(), State.SEND_FORM, user);
        } else {
            throw BusinessExceptions.APPLICATION_ACCEPTED;
        }
    }

    public void readyToCvSend(User user) {
        State userState = user.getCurrentState();;
        CVInfo previousCvInfo = cvInfoRepository.findTopByOwnerOrderByCreatedDateDesc(user);
        if (userState == State.SEND_CV) {
            user.setCurrentState(State.SEND_SECOND_CV);
        } else if (userState == State.SEND_SECOND_CV) {
            checkApplicationResult(previousCvInfo, penaltyTimeConfig.getShortPenaltyTime(), State.SEND_SECOND_CV, user);
            user.setCurrentState(State.SECOND_CV_SENT);
        } else if (userState == State.SECOND_CV_SENT) {
            checkApplicationResult(previousCvInfo, penaltyTimeConfig.getLongPenaltyTime(), State.SEND_FORM, user);
            throw BusinessExceptions.CV_REJECTED_NEW_FORM_REQUIRED;
        } else {
            throw BusinessExceptions.FORM_NOT_FOUND;
        }
    }

    private void checkApplicationResult(ApplicationEntity application, int penaltyTime, State previousState, User user) {
        switch (application.getResult()) {
            case ACCEPTED:
                return;
            case WAITING_FOR_ACCEPTANCE:
                throw BusinessExceptions.WAITING_APPLICATION_EXIST;
            case REJECTED:
                LocalDate penaltyDate = LocalDate.from(application.getCreatedDate()
                        .plusDays(penaltyTime));
                assertPenaltyTime(penaltyDate, previousState, user);
        }
    }

    private int getCvInfoCount(User owner) {
        List<CVInfo> cvInfoList = cvInfoRepository.findAllByOwner(owner);
        return cvInfoList.size();
    }

    private int getFormCount(User owner) {
        List<Form> formList = formRepository.findAllByOwner(owner);
        return formList.size();
    }

    private void assertPenaltyTime(LocalDate penaltyDate, State state, User user) {
        if (penaltyDate.isAfter(LocalDate.now())) {
            throw BusinessExceptions.APPLICATION_PENALTY;
        } else {
            user.setCurrentState(state);
        }
    }

    public void assertState(State state, State userState, BusinessException exception) {
        if (!state.equals(userState)) throw exception;
    }

    public void setCitationCountOfResearcher(User user) {
        Form previousForm = formRepository.findTopByOwnerOrderByCreatedDateDesc(user);
        if (!previousForm.getExternalApiId().isEmpty()) {
            String citation_count = getCitationCountFromFastAPI(previousForm.getExternalApiId());
            MetadataRegistry metadataRegistry = metadataRegistryRepository.findById("e8f2a71d-4b9c-4a7e-8c3f-5d1a8f9b2c6e")
                    .orElseThrow(() -> BusinessExceptions.REGISTRY_TYPE_NOT_FOUND);
            MetadataValue metadataValue = new MetadataValue(user, metadataRegistry, citation_count);
            metadataValueRepository.save(metadataValue);
        }
    }
}
