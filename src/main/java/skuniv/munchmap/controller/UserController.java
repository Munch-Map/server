package skuniv.munchmap.controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import skuniv.munchmap.config.exception.BadRequestException;
import skuniv.munchmap.domain.User;
import skuniv.munchmap.dto.StoreResponse;
import skuniv.munchmap.dto.UserRequest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import skuniv.munchmap.service.StoreService;
import skuniv.munchmap.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(name = "UserController", description = "User 관련 기능 | 회원가입, 로그인, 마이페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final StoreService storeService;

    @PostMapping("/register")
    @Operation(summary = "회원가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입이 성공적으로 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 회원가입 실패"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류"),
    })
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody UserRequest.userInfo userInfo) {
        try {
            User savedUser = userService.registerUser(userInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공: "  + savedUser.getName());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청으로 인한 회원가입 실패: " + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류로 인해 회원가입에 실패했습니다.");
        }
    }

    @PostMapping("/register/{userId}/chooseFavor")
    @Operation(summary = "회원가입 시, 카테고리 선택 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리가 성공적으로 저장되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인해 데이터 저장 오류"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류"),
    })
    public ResponseEntity<String> chooseFavor(
            @RequestBody @Valid UserRequest.userFavor userFavor,
            @PathVariable Long userId) {
        try {
            List<String> savedFavor = userService.chooseUserFavor(userFavor, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body("카테고리 선택이 성공적으로 완료되었습니다."  + savedFavor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청으로 인한 실패: " + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류로 인해 카테고리 선택에 실패했습니다.");
        }
    }


    @PostMapping("/login")
    @Operation(summary = "로그인 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<String> login(
            @RequestBody UserRequest.loginRequestDTO loginRequest,
            HttpSession session) {
        try {
            // 서비스 호출 (세션 정보 설정)
            userService.login(loginRequest, session);

            // 성공 응답 반환
            return ResponseEntity.ok("로그인 성공");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청으로 인한 로그인 실패: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류로 인해 로그인에 실패했습니다.");
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<String> logout(HttpSession session) {
        try{
            userService.logout(session);
            return ResponseEntity.ok("로그아웃 성공");
        } catch(BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청으로 인한 로그아웃 실패: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류로 인해 로그아웃에 실패");
        }
    }

    @GetMapping("/{userId}/stores/recommended")
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
                stores = userService.getRandomStores(lastStoreId);
            } else {
                stores = userService.getFilteredStores(userId, lastStoreId);
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
