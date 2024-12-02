package skuniv.munchmap.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import skuniv.munchmap.domain.Category;
import skuniv.munchmap.domain.Store;

import java.util.Collection;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s WHERE s.storeId > :lastStoreId ORDER BY RAND()")
    List<Store> findRandomStoresWithCursor(Long lastStoreId, Pageable pageable);

    @Query("SELECT s FROM Store s " +
            "WHERE s.category.categoryId = :categoryId " +
            "AND s.storeId > :lastStoreId " +
            "ORDER BY s.storeId ASC")
    List<Store> findStoresByCategoryWithCursor(@Param("categoryId") Long categoryId, @Param("lastStoreId") Long lastStoreId, Pageable pageable);
}
