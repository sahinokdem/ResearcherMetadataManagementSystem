package com.sahinokdem.researcher_metadata.repository;

import com.sahinokdem.researcher_metadata.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.Optional;

@NoRepositoryBean
public interface EntityWithOwnerRepository<T> extends JpaRepository<T, String> {
     Optional<T> findByOwnerAndId(User owner, String id);
     Page<T> findAllByOwner(User owner, Pageable pageable);
}
