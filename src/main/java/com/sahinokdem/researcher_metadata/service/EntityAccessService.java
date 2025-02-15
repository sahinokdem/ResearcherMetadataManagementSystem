package com.sahinokdem.researcher_metadata.service;

import com.sahinokdem.researcher_metadata.entity.BaseEntity;
import com.sahinokdem.researcher_metadata.entity.User;
import com.sahinokdem.researcher_metadata.enums.UserRole;
import com.sahinokdem.researcher_metadata.exception.BusinessException;
import com.sahinokdem.researcher_metadata.exception.BusinessExceptions;
import com.sahinokdem.researcher_metadata.repository.EntityWithOwnerRepository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

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

    public <T extends BaseEntity> Page<T> getAllEntities(EntityWithOwnerRepository<T> entityRepository,
                                                         UserRole ownerUserRole, UserRole accessUserRole, Pageable pageable) {
        if (userRoleService.checkCurrentUserRole(ownerUserRole)) {
            User owner = authenticationService.getAuthenticatedUser();
            return entityRepository.findAllByOwner(owner, pageable);
        } else if (userRoleService.checkCurrentUserRole(accessUserRole)) {
            return entityRepository.findAll(pageable);
        } else {
            throw BusinessExceptions.AUTHORIZATION_MISSING;
        }
    }
}
