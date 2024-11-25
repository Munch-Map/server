package skuniv.munchmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import skuniv.munchmap.domain.Category;
import skuniv.munchmap.domain.UserFavorCategory;

import java.util.List;

public interface UserFavorCategoryRepository extends JpaRepository<UserFavorCategory, Long> {

    List<UserFavorCategory> findUserFavorCategoryId(Long userId);
}
