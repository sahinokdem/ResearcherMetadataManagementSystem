package com.sahinokdem.researcher_metadata.entity;


import com.sahinokdem.researcher_metadata.enums.State;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String email;
    private String passwordEncoded;
    private UserRole userRole;
    private State currentState;
}
