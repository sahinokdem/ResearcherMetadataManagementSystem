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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@DFA
@Service
public class JobApplicationService {

    private final PenaltyTimeConfig penaltyTimeConfig;
    private final FormRepository formRepository;
    private final CVInfoRepository cvInfoRepository;
    @Setter
    @Getter
    private State currentState;

    public JobApplicationService(PenaltyTimeConfig penaltyTimeConfig,
             FormRepository formRepository, CVInfoRepository cvInfoRepository) {
        this.penaltyTimeConfig = penaltyTimeConfig;
        this.formRepository = formRepository;
        this.cvInfoRepository = cvInfoRepository;
        this.currentState = State.Q0;
    }

    public void readyToFormSend(User user) {
        assertState(State.Q0, BusinessExceptions.APPLICATION_ACCEPTED);
        Form previousForm = null;
        int formCount = getFormCount(user);
        if (formCount == 0) {
            setCurrentState(State.Q0);
            return;
        } else previousForm = formRepository.findAllByOwner(user).get(formCount - 1);
        checkApplicationResult(previousForm, penaltyTimeConfig.getLongPenaltyTime(), State.Q0);
    }

    public void readyToCvSend(User user) {
        int cvCount = getCvInfoCount(user);
        CVInfo previousCvInfo = cvInfoRepository.findByOwner(user).orElse(null);
        if (previousCvInfo == null) assertState(State.Q1, BusinessExceptions.FORM_NOT_FOUND);
        else if (cvCount == 1) {
            checkApplicationResult(previousCvInfo, penaltyTimeConfig.getShortPenaltyTime(), State.Q1);
        } else checkApplicationResult(previousCvInfo, penaltyTimeConfig.getLongPenaltyTime(), State.Q0);
    }

    private void checkApplicationResult(ApplicationEntity application, int penaltyTime, State previousState) {
        switch (application.getResult()) {
            case ACCEPTED:
                setCurrentState(State.Q1);
                throw BusinessExceptions.APPLICATION_ACCEPTED;
            case WAITING_FOR_ACCEPTANCE:
                throw BusinessExceptions.WAITING_APPLICATION_EXIST;
            case REJECTED:
                LocalDate penaltyDate = LocalDate.from(application.getCreatedDate()
                        .plusDays(penaltyTime));
                assertPenaltyTime(penaltyDate, previousState);
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

    private void assertPenaltyTime(LocalDate penaltyDate, State state) {
        if (penaltyDate.isBefore(LocalDate.now())) {
            throw BusinessExceptions.APPLICATION_PENALTY;
        } else setCurrentState(state);
    }

    public void assertState(State state, BusinessException exception) {
        if (!state.equals(currentState)) throw exception;
    }
}
