package com.demisnack.Eduplay.Application.catalog.repository;

import com.demisnack.Eduplay.Application.catalog.entity.CatalogEntity;
import com.demisnack.Eduplay.Application.catalog.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PurchaseRepository  extends JpaRepository<PurchaseEntity, UUID> {

    boolean existsByUserIdAndContentId(UUID userId, UUID contentId);

    List<PurchaseEntity> findAllByUserId(UUID userId);

    List<PurchaseEntity> findAllByContentIdIn(List<UUID> contentIds);
}
