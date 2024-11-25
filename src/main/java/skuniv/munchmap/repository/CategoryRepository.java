package skuniv.munchmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skuniv.munchmap.domain.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {

}
