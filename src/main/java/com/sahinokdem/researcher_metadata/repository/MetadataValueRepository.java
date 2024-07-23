package com.sahinokdem.researcher_metadata.repository;

import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetadataValueRepository extends JpaRepository<MetadataValue, String> {

}