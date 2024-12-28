package skuniv.munchmap.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skuniv.munchmap.service.MapService;

import java.util.Map;

@Tag(name = "MapController", description = "Map 관련 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/maps")
public class MapController {

    @GetMapping("/place/{userId}")
    public String getUserLocationMap() {
        // Kakao Map HTML 반환
        return "map"; // resources/static/map.html로 매핑
    }
}
