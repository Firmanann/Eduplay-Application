package com.demisnack.Eduplay.Application.catalog.entity;

import com.demisnack.Eduplay.Application.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contributor_id", nullable = false)
    private UserEntity contributor;

    @Column(name = "content_type", nullable = false, length = 50)
    private String contentType; // GAME, MODULE

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String pricing; // FREE, PAID

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "APPROVED";

    @Column(length = 50)
    private String subject;

    @Column(name = "grade_level", length = 20)
    private String gradeLevel;

    @Column(name = "file_url", columnDefinition = "TEXT")
    private String fileUrl;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}