package com.sahinokdem.researcher_metadata.entity;

import com.sahinokdem.researcher_metadata.enums.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Form extends ApplicationEntity {
    private String nameAndSurname;
    private String email;
    private LocalDate dateOfBirth;
    private String externalApiId;
}
