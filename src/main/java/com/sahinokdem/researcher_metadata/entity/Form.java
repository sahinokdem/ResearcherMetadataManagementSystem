package com.sahinokdem.researcher_metadata.entity;

import com.sahinokdem.researcher_metadata.enums.FormAndCvResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Form extends BaseEntity {
    private String nameAndSurname;
    private String email;
    private String externalApiId;
    private String userId;
    private FormAndCvResult result;
}
