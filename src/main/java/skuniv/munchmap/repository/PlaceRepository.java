package skuniv.munchmap.repository;

import skuniv.munchmap.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByStoreId(int storeId); // 특정 Store에 연결된 Place 조회
}
