package skuniv.munchmap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import skuniv.munchmap.config.exception.BadRequestException;
import skuniv.munchmap.config.exception.UnauthorizedException;
import skuniv.munchmap.domain.Store;
import skuniv.munchmap.domain.User;
import skuniv.munchmap.service.StoreService;

import java.util.List;

@Tag(name = "Store Controller(가게 API)")
@Controller
@RequestMapping("/api/v1/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("/recommend")
    @Operation(summary = "추천 식당 목록 조회", description = "추천 식당을 반환.")
    public ResponseEntity<List<Store>> getRecommendedStores(@RequestParam String recommendType, HttpSession session) {

        // 세션에서 현재 로그인한 사용자 정보 가져오기
        User loginUser = (User) session.getAttribute("user");

        // 로그인하지 않은 경우
        if (loginUser == null) {
            throw new UnauthorizedException("You are not logged in.");
        }

        // 로그인한 사용자의 선호 음식 불러오기
        String favor = loginUser.getFavor();

        // 스토어 리스트 변수
        List<Store> recommendedStores;

        // preferred = 사용자 맞춤, random = 랜덤
        if ("preferred".equalsIgnoreCase(recommendType)) {
            if (favor == null) {
                throw new BadRequestException("User's preferred category is not set.");  // 예외 던지기
            }
            recommendedStores = storeService.recommendPreferred(favor); // 선호 음식 기반 추천
            return ResponseEntity.ok(recommendedStores);
        } else if ("random".equalsIgnoreCase(recommendType)) {
            recommendedStores = storeService.recommendRandom(); // 랜덤 추천
            return ResponseEntity.ok(recommendedStores);
        } else {
            // 잘못된 recommendationType
            throw new BadRequestException("Invalid Type");
        }
    }
}
