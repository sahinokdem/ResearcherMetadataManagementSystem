package com.sahinokdem.researcher_metadata.entity;

import com.sahinokdem.researcher_metadata.enums.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public class ApplicationEntity extends BaseEntity{
    @ManyToOne
    private User owner;
    private Result result;
    private String reason;
}
