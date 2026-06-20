package com.demisnack.Eduplay.Application.catalog.service;

import com.demisnack.Eduplay.Application.catalog.dto.CatalogResponse;
import com.demisnack.Eduplay.Application.catalog.dto.PurchaseResponse;
import com.demisnack.Eduplay.Application.catalog.dto.TaxonomyResponse;
import com.demisnack.Eduplay.Application.catalog.entity.CatalogEntity;
import com.demisnack.Eduplay.Application.catalog.entity.PurchaseEntity;
import com.demisnack.Eduplay.Application.catalog.repository.CatalogRepository;
import com.demisnack.Eduplay.Application.catalog.repository.PurchaseRepository;
import com.demisnack.Eduplay.Application.global.exception.BusinessException;
import com.demisnack.Eduplay.Application.global.exception.ErrorCode;
import com.demisnack.Eduplay.Application.user.entity.UserEntity;
import com.demisnack.Eduplay.Application.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;

    public List<CatalogResponse> getCatalog(String category, String subject) {

        //Get all content catalog
        return catalogRepository.findCatalogWithFilter(category, subject)
                .stream()
                .map(content -> CatalogResponse.builder()
                        .id(content.getId().toString())
                        .title(content.getTitle())
                        .price(content.getPrice())
                        .thumbnailUrl(content.getThumbnailUrl())
                        .category(content.getCategory())
                        .subject(content.getSubject())
                        .contributorName(content.getContributor().getName())
                        .build())
                .toList();
    }

    //Get 1 content
    public CatalogResponse getCatalogDetail(String id) {
        CatalogEntity catalog = catalogRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_NOT_FOUND));

        return CatalogResponse.builder()
                .id(catalog.getId().toString())
                .title(catalog.getTitle())
                .description(catalog.getDescription())
                .price(catalog.getPrice())
                .thumbnailUrl(catalog.getThumbnailUrl())
                .category(catalog.getCategory())
                .subject(catalog.getSubject())
                .gradeLevel(catalog.getGradeLevel())
                .contributorName(catalog.getContributor().getName())
                .createdAt(catalog.getCreatedAt().toLocalDateTime())
                .build();
    }

    //Get category
    public TaxonomyResponse getTaxonomies() {
        return TaxonomyResponse.builder()
                .categories(List.of("GAME", "QUIZ", "INTERACTIVE"))
                .subjects(List.of("Matematika", "Sejarah", "Sains"))
                .gradeLevels(List.of("SD", "SMP", "SMA"))
                .build();
    }

    @Transactional
    public PurchaseResponse purchaseCatalog(String catalogId, String userEmail) {

        //1. Cari data pembeli
        UserEntity buyer = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        //2.Cari data game
        CatalogEntity catalog = catalogRepository.findById(UUID.fromString(catalogId))
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_NOT_FOUND));

        //3.Validasi User - Game yang sama
        if (purchaseRepository.existsByUserIdAndContentId(buyer.getId(), catalog.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_OWNED);
        }

        //4. Validasi saldo user
        if (buyer.getBalance() < catalog.getPrice()) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        //5. Proses Transaksi
        buyer.setBalance(buyer.getBalance() - catalog.getPrice());

        UserEntity creator = catalog.getContributor();
        creator.setBalance(creator.getBalance() + catalog.getPrice());

        userRepository.save(buyer);
        userRepository.save(creator);

        //6. save pembelian
        PurchaseEntity purchase = PurchaseEntity.builder()
                .userId(buyer.getId())
                .contentId(catalog.getId())
                .pricePaid(catalog.getPrice())
                .build();
        purchaseRepository.save(purchase);

        //7. Mapping desain
        return PurchaseResponse.builder()
                .purchaseId(purchase.getId().toString())
                .contentId(catalog.getId().toString())
                .pricePaid(purchase.getPricePaid())
                .build();
    }


}
