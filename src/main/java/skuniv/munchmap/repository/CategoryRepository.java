package skuniv.munchmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import skuniv.munchmap.domain.Category;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.categoryId IN :categoryIds")
    List<Category> findAllByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
}
