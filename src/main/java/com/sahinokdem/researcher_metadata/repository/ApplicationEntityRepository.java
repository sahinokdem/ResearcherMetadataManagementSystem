package com.sahinokdem.researcher_metadata.repository;

import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.Result;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.Optional;

@NoRepositoryBean
public interface ApplicationEntityRepository<T> extends EntityWithOwnerRepository<T> {
    Optional<T> findByOwnerAndResult(User owner, Result result);
    T findTopByOwnerOrderByCreatedDateDesc(User user);
}
