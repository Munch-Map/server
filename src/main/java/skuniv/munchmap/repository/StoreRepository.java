package skuniv.munchmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import skuniv.munchmap.domain.Store;
import skuniv.munchmap.domain.UserFavorCategory;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "SELECT s FROM Store s JOIN s.storeCategory c " +
            "WHERE c IN :categories AND s.storeId > :lastStoreId " +
            "ORDER BY s.storeId ASC")
    List<Store> findStoresByCategoriesWithCursor(@Param("categories") List<UserFavorCategory> categories, @Param("lastStoreId") Long lastStoreId, @Param("limit") int limit);

    @Query(value = "SELECT * FROM store s WHERE s.store_id > :lastStoreId ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Store> findRandomStoresWithCursor(@Param("lastStoreId") Long lastStoreId, @Param("limit") int limit);

}
