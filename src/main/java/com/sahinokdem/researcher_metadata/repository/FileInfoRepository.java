package com.sahinokdem.researcher_metadata.repository;

import com.sahinokdem.researcher_metadata.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, String> {

    Optional<FileInfo> findByName(String fileName);
}
