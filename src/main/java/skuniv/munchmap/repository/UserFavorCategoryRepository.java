package skuniv.munchmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import skuniv.munchmap.domain.UserFavorCategory;

import java.util.List;

public interface UserFavorCategoryRepository extends JpaRepository<UserFavorCategory, Long> {

    @Query("SELECT uf FROM UserFavorCategory uf " +
            "WHERE uf.user.userId = :userId")
    List<UserFavorCategory> findByUserId(@Param("userId") Long userId);
}
