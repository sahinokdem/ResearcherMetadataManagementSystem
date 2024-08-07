package com.sahinokdem.researcher_metadata.entity;

import com.sahinokdem.researcher_metadata.enums.FormAndCvResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Form extends BaseEntity {
    private String nameAndSurname;
    private String email;
    private LocalDate dateOfBirth;
    private String externalApiId;
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private User owner;
    private FormAndCvResult result;
}
