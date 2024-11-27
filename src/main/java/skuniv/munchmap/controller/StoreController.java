package skuniv.munchmap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skuniv.munchmap.dto.StoreResponse;
import skuniv.munchmap.service.StoreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/stores")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{userId}/favor-store")
    @Operation(summary = "사용자 기반 추천 음식점 목록 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<Map<String, Object>> getRecommendedStores(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean random,
            @RequestParam(required = false, defaultValue = "0") Long lastStoreId) {
        try {
            List<StoreResponse.StoreResponseDTO> stores;

            // 랜덤으로 조회할지 여부 결정
            if (random) {
                stores = storeService.getRandomStores(lastStoreId);
            } else {
                stores = storeService.getFilteredStores(userId, lastStoreId);
            }

            // 응답 메시지와 데이터를 Map에 담아서 반환
            Map<String, Object> response = new HashMap<>();
            response.put("message", "추천 음식점 목록 조회 성공");
            response.put("stores", stores);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "잘못된 요청: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "서버 내부 오류");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}