package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.BaseEntity;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessException;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.repository.EntityWithOwnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class EntityAccessService {

    private final UserRoleService userRoleService;
    private final AuthenticationService authenticationService;

    public <T extends BaseEntity> T getEntity(String entityId, EntityWithOwnerRepository<T> entityRepository,
              UserRole ownerUserRole, UserRole accessUserRole,
              BusinessException entityNotFoundException) {
        if (userRoleService.checkCurrentUserRole(ownerUserRole)) {
            User owner = authenticationService.getAuthenticatedUser();
            return entityRepository.findByOwnerAndId(owner, entityId)
                    .orElseThrow(() -> entityNotFoundException);
        } else if (userRoleService.checkCurrentUserRole(accessUserRole)) {
            return entityRepository.findById(entityId)
                    .orElseThrow(() -> entityNotFoundException);
        } else {
            throw BusinessExceptions.AUTHORIZATION_MISSING;
        }
    }
}
