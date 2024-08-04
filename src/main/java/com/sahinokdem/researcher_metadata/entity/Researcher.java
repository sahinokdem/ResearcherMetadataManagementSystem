package com.sahinokdem.researcher_metadata.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "researcher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Researcher extends BaseEntity{

    private String userId;
    private String name;
    private Integer citationCount;
}
