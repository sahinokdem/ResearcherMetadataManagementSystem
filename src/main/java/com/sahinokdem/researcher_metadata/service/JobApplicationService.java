package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.config.PenaltyTimeConfig;
import com.sahinokdem.researcher_metadata.entity.*;
import com.sahinokdem.researcher_metadata.enums.Result;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.repository.CVInfoRepository;
import com.sahinokdem.researcher_metadata.repository.FormRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class JobApplicationService {

    private final PenaltyTimeConfig penaltyTimeConfig;
    private final FormRepository formRepository;
    private final CVInfoRepository cvInfoRepository;

    public void readyToFormSend(User user) {
        Form form = formRepository.findByOwner(user).orElse(null);
        if (form == null) return;
        switch (form.getResult()) {
            case ACCEPTED:
                throw BusinessExceptions.FORM_ACCEPTED;
            case WAITING_FOR_ACCEPTANCE:
                throw BusinessExceptions.WAITING_APPLICATION_EXIST;
            case REJECTED:
                LocalDate penaltyDate = LocalDate.from(form.getCreatedDate()
                        .plusDays(penaltyTimeConfig.getLongPenaltyTime()));
                assertPenaltyTime(penaltyDate, user);
        }
    }

    public void readyToCvSend(User user) {
        Form form = formRepository.findByOwnerAndResult(user, Result.ACCEPTED)
                .orElseThrow(() -> BusinessExceptions.FORM_NOT_FOUND);
        if (getCvInfoCount(user) != 0) {
            LocalDate penaltyDate = LocalDate.from(form.getCreatedDate()
                    .plusDays(setCvPenaltyTime(user)));
            assertPenaltyTime(penaltyDate, user);
        }
    }

    private void assertPenaltyTime(LocalDate penaltyDate, User owner) {
        if (penaltyDate.isBefore(LocalDate.now())) throw BusinessExceptions.APPLICATION_PENALTY;
        else {
            cvInfoRepository.deleteAllByOwner(owner);
            formRepository.deleteByOwner(owner);
        }
    }

    private int setCvPenaltyTime(User owner) {
        if (getCvInfoCount(owner) == 2) {
            return penaltyTimeConfig.getLongPenaltyTime();
        } else return penaltyTimeConfig.getShortPenaltyTime(); // CVInfo count == 1
    }

    private int getCvInfoCount(User owner) {
        List<CVInfo> cvInfoList = cvInfoRepository.findAllByOwner(owner);
        System.out.println(cvInfoList.size());
        return cvInfoList.size();
    }
}
