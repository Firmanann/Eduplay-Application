package com.demisnack.Eduplay.Application.catalog.repository;

import com.demisnack.Eduplay.Application.catalog.entity.CatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatalogRepository extends JpaRepository<CatalogEntity, UUID> {

    //query menampilkan semua
    @Query("SELECT c FROM CatalogEntity c JOIN FETCH c.contributor WHERE " +
            "(:category IS NULL OR c.category = :category) AND " +
            "(:subject IS NULL OR c.subject = :subject)")
    List<CatalogEntity> findCatalogWithFilter(
            @Param("category") String category,
            @Param("subject") String subject
    );

    List<CatalogEntity> findAllByContributorId(UUID contributorId);



}
