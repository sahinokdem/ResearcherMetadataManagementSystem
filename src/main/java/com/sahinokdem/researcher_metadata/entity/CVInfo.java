package com.sahinokdem.researcher_metadata.entity;

import com.sahinokdem.researcher_metadata.enums.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cv_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CVInfo extends ApplicationEntity {
    @OneToOne
    private FileInfo fileInfo;
    private String cvAssociation;
}
