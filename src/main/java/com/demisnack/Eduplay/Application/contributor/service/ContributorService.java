package com.demisnack.Eduplay.Application.contributor.service;

import com.demisnack.Eduplay.Application.catalog.entity.CatalogEntity;
import com.demisnack.Eduplay.Application.catalog.entity.PurchaseEntity;
import com.demisnack.Eduplay.Application.catalog.repository.CatalogRepository;
import com.demisnack.Eduplay.Application.catalog.repository.PurchaseRepository;
import com.demisnack.Eduplay.Application.contributor.dto.*;
import com.demisnack.Eduplay.Application.global.exception.BusinessException;
import com.demisnack.Eduplay.Application.global.exception.ErrorCode;
import com.demisnack.Eduplay.Application.user.entity.UserEntity;
import com.demisnack.Eduplay.Application.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContributorService {

    private final UserRepository userRepository;
    private final CatalogRepository catalogRepository;
    private final PurchaseRepository purchaseRepository;

    public void createContent(CreateContentRequest request, String email) {

        // 1. Cari data kreator yang lagi login
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. Mapping request ke Entity Database
        CatalogEntity newContent = CatalogEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .fileUrl(request.getFileUrl())
                .thumbnailUrl(request.getThumbnailUrl())
                .category(request.getCategory())
                .subject(request.getSubject())
                .gradeLevel(request.getGradeLevel())
                .contributor(contributor) // Set relasi ke user yang bikin
                .build();

        // 3. Save ke tabel catalogs
        catalogRepository.save(newContent);
    }

    public List<MyContentResponse> getMyContents(String email) {

        // 1. Cari data kreator
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. Tarik semua game buatan dia dari database
        List<CatalogEntity> myContents = catalogRepository.findAllByContributorId(contributor.getId());

        // 3. Mapping ke response
        return myContents.stream().map(catalog -> MyContentResponse.builder()
                .id(catalog.getId().toString())
                .title(catalog.getTitle())
                .price(catalog.getPrice())
                .createdAt(catalog.getCreatedAt().toLocalDateTime())
                .build()).toList();
    }

    @Transactional
    public void updateContent(String catalogId, UpdateContentRequest request, String email) {
        // 1. Cari data kreator
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. Cari game yang mau diedit
        CatalogEntity catalog = catalogRepository.findById(UUID.fromString(catalogId))
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_NOT_FOUND));

        // 3. Validasi Keamanan: Pastikan yang ngedit adalah pemilik asli gamenya!
        if (!catalog.getContributor().getId().equals(contributor.getId())) {
            throw new BusinessException(ErrorCode.NOT_OWNED); // Kita pakai error yang sama kayak di library aja
        }

        // 4. Timpa data lama dengan data baru
        catalog.setTitle(request.getTitle());
        catalog.setDescription(request.getDescription());
        catalog.setPrice(request.getPrice());

        // 5. Save (sebenernya otomatis tersave karena ada @Transactional, tapi biar eksplisit aja)
        catalogRepository.save(catalog);
    }

    @Transactional
    public void deleteContent(String catalogId, String email) {
        // 1. Cari data kreator
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. Cari game yang mau dihapus
        CatalogEntity catalog = catalogRepository.findById(UUID.fromString(catalogId))
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_NOT_FOUND));

        // 3. Validasi Keamanan: Pastikan yang ngehapus adalah pemilik asli gamenya!
        if (!catalog.getContributor().getId().equals(contributor.getId())) {
            throw new BusinessException(ErrorCode.NOT_OWNED);
        }

        // 4. Hapus dari database
        catalogRepository.delete(catalog);
    }

    public BalanceResponse getBalance(String email) {

        // 1. Cari data user yang lagi login
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. Mapping data saldo dan bank ke DTO
        return BalanceResponse.builder()
                .balance(contributor.getBalance())
                .bankName(contributor.getBankName()) // Pastikan field ini ada di UserEntity
                .bankAccount(contributor.getBankAccount()) // Pastikan field ini ada di UserEntity
                .build();
    }

    public List<TransactionResponse> getTransactions(String email) {

        // 1. Cari data kreator
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. Tarik semua game buatan kreator ini
        List<CatalogEntity> myContents = catalogRepository.findAllByContributorId(contributor.getId());

        // Ambil ID game-gamenya aja untuk pencarian
        List<UUID> contentIds = myContents.stream().map(CatalogEntity::getId).toList();

        // Kalau belum punya game sama sekali, langsung return list kosong
        if (contentIds.isEmpty()) {
            return List.of();
        }

        // 3. Tarik semua struk pembelian yang contentId-nya ada di list tadi
        List<PurchaseEntity> purchases = purchaseRepository.findAllByContentIdIn(contentIds);

        // 4. Mapping data ke Response DTO
        return purchases.stream().map(purchase -> {

            // Cari data pembeli (buyer)
            UserEntity buyer = userRepository.findById(purchase.getUserId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // Cocokin judul game dari data myContents yang udah ditarik di langkah 2
            String title = myContents.stream()
                    .filter(c -> c.getId().equals(purchase.getContentId()))
                    .findFirst()
                    .map(CatalogEntity::getTitle)
                    .orElse("Unknown Game");

            //Desain response
            return TransactionResponse.builder()
                    .purchaseId(purchase.getId().toString())
                    .contentTitle(title)
                    .buyerName(buyer.getName())
                    .pricePaid(purchase.getPricePaid())
                    .purchasedAt(purchase.getCreatedAt().toLocalDateTime())
                    .build();
        }).toList();
    }
}