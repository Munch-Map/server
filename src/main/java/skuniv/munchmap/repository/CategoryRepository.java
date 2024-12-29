package skuniv.munchmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skuniv.munchmap.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // 특정 favorId를 통해 Category 조회하는 메서드
    Category findByFavorId(String favorId);
}
