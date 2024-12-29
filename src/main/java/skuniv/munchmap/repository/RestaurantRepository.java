package skuniv.munchmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skuniv.munchmap.domain.Category;
import skuniv.munchmap.domain.Store;
import skuniv.munchmap.domain.User;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Store, Long> {
    // 특정 음식 종류에 해당하는 식당들을 찾는 메서드 정의
    List<Store> findBycategory(Category category);
}
