package com.sahinokdem.researcher_metadata.repository;

import com.sahinokdem.researcher_metadata.entity.MetadataRegistry;
import com.sahinokdem.researcher_metadata.entity.MetadataValue;
import com.sahinokdem.researcher_metadata.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetadataValueRepository extends JpaRepository<MetadataValue, String> {

    Optional<MetadataValue> findByOwnerAndId(User owner, String id);
    List<MetadataValue> findAllByOwnerAndMetadataRegistry(User owner, MetadataRegistry metadataRegistry);
    List<MetadataValue> findAllByMetadataRegistry(MetadataRegistry metadataRegistry);
    List<MetadataValue> findAllByOwner(User owner);
}