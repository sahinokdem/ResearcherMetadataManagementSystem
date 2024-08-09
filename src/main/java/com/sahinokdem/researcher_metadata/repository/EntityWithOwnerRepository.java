package com.sahinokdem.researcher_metadata.repository;

import com.sahinokdem.researcher_metadata.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface EntityWithOwnerRepository<T> extends JpaRepository<T, String> {
     Optional<T> findByOwnerAndId(User owner, String id);
     List<T> findAllByOwner(User owner);
     Optional<T> findByOwner(User owner);
     void deleteAllByOwner(User owner);
     void deleteByOwner(User owner);
}
