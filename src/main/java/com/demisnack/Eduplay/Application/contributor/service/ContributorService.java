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

        // 1.Cari data kreator
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2.Mapping request - entity - db
        CatalogEntity newContent = CatalogEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .fileUrl(request.getFileUrl())
                .thumbnailUrl(request.getThumbnailUrl())
                .category(request.getCategory())
                .subject(request.getSubject())
                .gradeLevel(request.getGradeLevel())
                .contributor(contributor)
                .build();

        // 3.Save ke tabel catalogs
        catalogRepository.save(newContent);
    }

    public List<MyContentResponse> getMyContents(String email) {

        // 1.Cari data kreator
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2.Ambil semua data dari db
        List<CatalogEntity> myContents = catalogRepository.findAllByContributorId(contributor.getId());

        // 3.Mapping response
        return myContents.stream().map(catalog -> MyContentResponse.builder()
                .id(catalog.getId().toString())
                .title(catalog.getTitle())
                .price(catalog.getPrice())
                .createdAt(catalog.getCreatedAt().toLocalDateTime())
                .build()).toList();
    }

    @Transactional
    public void updateContent(String catalogId, UpdateContentRequest request, String email) {
        //1.Cari data kreator
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        //2. Cari game
        CatalogEntity catalog = catalogRepository.findById(UUID.fromString(catalogId))
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_NOT_FOUND));

        //3.Validasi contributor
        if (!catalog.getContributor().getId().equals(contributor.getId())) {
            throw new BusinessException(ErrorCode.NOT_OWNED);
        }

        //4.Update data lama
        catalog.setTitle(request.getTitle());
        catalog.setDescription(request.getDescription());
        catalog.setPrice(request.getPrice());
        catalog.setCategory(request.getCategory());
        catalog.setSubject(request.getSubject());
        catalog.setGradeLevel(request.getGradeLevel());
        catalog.setFileUrl(request.getFileUrl());
        catalog.setThumbnailUrl(request.getThumbnailUrl());

        //5.save data
        catalogRepository.save(catalog);
    }

    @Transactional
    public void deleteContent(String catalogId, String email) {

        //1.cari data kreator
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        //2.cari game
        CatalogEntity catalog = catalogRepository.findById(UUID.fromString(catalogId))
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_NOT_FOUND));

        //3.validasi contributor
        if (!catalog.getContributor().getId().equals(contributor.getId())) {
            throw new BusinessException(ErrorCode.NOT_OWNED);
        }

        //4.hapus dari database
        catalogRepository.delete(catalog);
    }

    public BalanceResponse getBalance(String email) {

        //cari data user
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        //mapping data response
        return BalanceResponse.builder()
                .balance(contributor.getBalance())
                .bankName(contributor.getBankName())
                .bankAccount(contributor.getBankAccount())
                .build();
    }

    public List<TransactionResponse> getTransactions(String email) {

        //1.cari data kreator
        UserEntity contributor = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        //2.tarik semua game buatan kreator ini
        List<CatalogEntity> myContents = catalogRepository.findAllByContributorId(contributor.getId());

        //3.ambil semua id game
        List<UUID> contentIds = myContents.stream().map(CatalogEntity::getId).toList();
        if (contentIds.isEmpty()) {
            return List.of();
        }

        //4.mapper catalog - purchase
        List<PurchaseEntity> purchases = purchaseRepository.findAllByContentIdIn(contentIds);

        //5.mapping DTO
        return purchases.stream().map(purchase -> {

            //cari data buyer
            UserEntity buyer = userRepository.findById(purchase.getUserId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            //validasi data game
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