package skuniv.munchmap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skuniv.munchmap.domain.Category;
import skuniv.munchmap.domain.Store;
import skuniv.munchmap.repository.CategoryRepository;
import skuniv.munchmap.repository.RestaurantRepository;

import java.util.Collections;
import java.util.List;

@Service
public class StoreService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public StoreService(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository) {
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
    }

    // 사용자 선호 음식 기반 추천`
    public List<Store> recommendPreferred(String favor) {

        // 아이디를 이용해 사용자 선호 음식 종류 찾기
        Category category = categoryRepository.findByFavorId(favor);

        if (category == null) {
            throw new IllegalArgumentException("Invalid category");
        }

        // 사용자 선호 음식 종류 식당 불러오기
        List<Store> preferredStores = restaurantRepository.findBycategory(category);
        Collections.shuffle(preferredStores);  // 무작위로 섞기
        return preferredStores;
    }

    // 랜덤 추천
    public List<Store> recommendRandom() {
        List<Store> allStores = restaurantRepository.findAll();
        Collections.shuffle(allStores);  // 무작위로 섞기
        return allStores;
    }
}
