package skuniv.munchmap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import skuniv.munchmap.domain.Store;

@Data
@Builder
public class StoreResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    @Schema(title="STORE_RES_01 : 가게 정보 응답 DTO")
    public static class StoreResponseDTO {
        private Long storeId;
        private String storeName;
        private String storeAddress;

        // Entity -> DTO 변환 메서드
        public static StoreResponseDTO fromEntity(Store store) {
            return StoreResponseDTO.builder()
                    .storeId(store.getStoreId())
                    .storeName(store.getStoreName())
                    .storeAddress(store.getStoreAddress())
                    .build();
        }
    }
}

