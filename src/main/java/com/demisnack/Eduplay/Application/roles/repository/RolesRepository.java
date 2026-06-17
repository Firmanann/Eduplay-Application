package com.demisnack.Eduplay.Application.roles.repository;

import com.demisnack.Eduplay.Application.roles.entity.RolesEntity;
import com.demisnack.Eduplay.Application.roles.entity.RolesStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RolesRepository extends JpaRepository<RolesEntity, UUID> {

    Optional<RolesEntity> findByStatus(String status);

}
