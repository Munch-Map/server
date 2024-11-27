package skuniv.munchmap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skuniv.munchmap.domain.UserFavorCategory;
import skuniv.munchmap.dto.StoreResponse;
import skuniv.munchmap.repository.CategoryRepository;
import skuniv.munchmap.repository.StoreRepository;
import skuniv.munchmap.repository.UserFavorCategoryRepository;
import skuniv.munchmap.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserFavorCategoryRepository userFavorCategoryRepository;

    // (1) 사용자가 선택한 카테고리로 필터링
    @Transactional
    public List<StoreResponse.StoreResponseDTO> getFilteredStores(Long userId, Long lastStoreId) {
        // 사용자의 선호 카테고리를 조회
        List<UserFavorCategory> favoriteCategories = userFavorCategoryRepository.findUserFavorCategoryId(userId);

        // 선호 카테고리가 없으면 랜덤으로 조회
        if (favoriteCategories.isEmpty()) {
            return getRandomStores(lastStoreId);
        }

        // 선호 카테고리를 기반으로 음식점 조회 (커서 페이징 적용)
        return storeRepository.findStoresByCategoriesWithCursor(favoriteCategories, lastStoreId, 10)
                .stream()
                .map(StoreResponse.StoreResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // (1)-1 랜덤으로 음식점 조회 (커서 페이징 적용)
    public List<StoreResponse.StoreResponseDTO> getRandomStores(Long lastStoreId) {
        return storeRepository.findRandomStoresWithCursor(lastStoreId, 10)
                .stream()
                .map(StoreResponse.StoreResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
