package skuniv.munchmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import skuniv.munchmap.domain.Category;
import skuniv.munchmap.domain.UserFavorCategory;

import java.util.List;

public interface UserFavorCategoryRepository extends JpaRepository<UserFavorCategory, Long> {

    @Query("SELECT ufc.category FROM UserFavorCategory ufc WHERE ufc.user = :userId")
    List<Long> findCategoryIdsByUserId(@Param("userId") Long userId);
}
