package com.sahinokdem.researcher_metadata.repository;

import com.sahinokdem.researcher_metadata.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRepository extends JpaRepository<Form, String> {
}
