package com.demisnack.Eduplay.Application.library.service;

import com.demisnack.Eduplay.Application.catalog.entity.CatalogEntity;
import com.demisnack.Eduplay.Application.catalog.entity.PurchaseEntity;
import com.demisnack.Eduplay.Application.catalog.repository.CatalogRepository;
import com.demisnack.Eduplay.Application.catalog.repository.PurchaseRepository;
import com.demisnack.Eduplay.Application.global.exception.BusinessException;
import com.demisnack.Eduplay.Application.global.exception.ErrorCode;
import com.demisnack.Eduplay.Application.library.dto.ContentDto;
import com.demisnack.Eduplay.Application.library.dto.DownloadResponse;
import com.demisnack.Eduplay.Application.library.dto.LibraryResponse;
import com.demisnack.Eduplay.Application.library.dto.PlayResponse;
import com.demisnack.Eduplay.Application.user.entity.UserEntity;
import com.demisnack.Eduplay.Application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final CatalogRepository catalogRepository;

    public List<LibraryResponse> getMyLibrary(String email) {

        // 1. Cari user login
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. Ambil semua riwayat pembelian dia
        List<PurchaseEntity> purchases = purchaseRepository.findAllByUserId(user.getId());

        // 3. Mapping data ke bentuk Response DTO
        return purchases.stream().map(purchase -> {
            // Tarik detail game berdasarkan contentId di struk pembelian
            CatalogEntity catalog = catalogRepository.findById(purchase.getContentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.GAME_NOT_FOUND));

            return LibraryResponse.builder()
                    .purchaseId(purchase.getId().toString())
                    .purchasedAt(purchase.getCreatedAt().toLocalDateTime())
                    .content(ContentDto.builder()
                            .id(catalog.getId().toString())
                            .title(catalog.getTitle())
                            .thumbnailUrl(catalog.getThumbnailUrl())
                            .build())
                    .build();
        }).toList();
    }

    public PlayResponse playGame(String catalogId, String email) {
        // 1. Cari user
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. Cari game
        CatalogEntity catalog = catalogRepository.findById(UUID.fromString(catalogId))
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_NOT_FOUND));

        // 3. Validasi Kepemilikan (Ini penting banget!)
        if (!purchaseRepository.existsByUserIdAndContentId(user.getId(), catalog.getId())) {
            throw new BusinessException(ErrorCode.NOT_OWNED);
        }

        // 4. Return URL
        return PlayResponse.builder()
                .fileUrl(catalog.getFileUrl())
                .build();
    }

    public DownloadResponse downloadGame(String catalogId, String email) {

        // 1. Cari user
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. Cari game
        CatalogEntity catalog = catalogRepository.findById(UUID.fromString(catalogId))
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_NOT_FOUND));

        // 3. Validasi Kepemilikan (Tetep wajib!)
        if (!purchaseRepository.existsByUserIdAndContentId(user.getId(), catalog.getId())) {
            throw new BusinessException(ErrorCode.NOT_OWNED);
        }

        // 4. Return URL
        return DownloadResponse.builder()
                .downloadUrl(catalog.getFileUrl())
                .build();
    }
}