package com.demisnack.Eduplay.Application.generategame.entity;

import com.demisnack.Eduplay.Application.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "generated_games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 50)
    private String type; // QUIZ_TIMER, DRAG_DROP_SEQUENCE

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "html_content", columnDefinition = "TEXT")
    private String htmlContent;

    @Column(name = "download_url", columnDefinition = "TEXT")
    private String downloadUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
}