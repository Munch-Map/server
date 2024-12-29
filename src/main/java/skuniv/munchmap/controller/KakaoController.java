package skuniv.munchmap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import skuniv.munchmap.dto.KakaoDocument;
import skuniv.munchmap.dto.KakaoResponse;
import skuniv.munchmap.service.KakaoMapService;
import java.util.List;
import java.util.Map;

@Tag(name = "Kakao Controller(카카오 API)")
@RestController
@RequestMapping("/api/kakao")
public class KakaoController {
    private final KakaoMapService kakaoMapService;

    public KakaoController(KakaoMapService kakaoMapService) {
        this.kakaoMapService = kakaoMapService;
    }

    @GetMapping("/search/address")
    @Operation(summary = "주소로 검색", description = "주소를 기반으로 좌표 정보를 반환합니다.")
    public Map<String, Object> getCoordinates(@RequestParam String address) {
        // Service에서 Map<String, Object> 반환받아 그대로 전달
        return kakaoMapService.searchByAddress(address);
    }

    @GetMapping("/search/place")
    @Operation(summary = "가게 이름으로 검색", description = "가게 이름을 변환.")
    public List<KakaoDocument> searchByPlace(@RequestParam String place) {
        return kakaoMapService.searchByPlace(place);
    }
}
