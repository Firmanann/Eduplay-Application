package com.demisnack.Eduplay.Application.catalog.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchases")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private UUID contentId;
    private Integer pricePaid;
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();}