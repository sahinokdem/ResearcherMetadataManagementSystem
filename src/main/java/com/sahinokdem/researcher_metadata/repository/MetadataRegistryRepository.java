package com.sahinokdem.researcher_metadata.repository;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.enums.MetadataRegistryType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MetadataRegistryRepository extends JpaRepository<MetadataRegistry, String> {

    Optional<MetadataRegistry> findByName(String name);
}
