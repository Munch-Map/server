package skuniv.munchmap.controller;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import skuniv.munchmap.config.exception.BadRequestException;
import skuniv.munchmap.domain.User;
import skuniv.munchmap.dto.UserRequest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import skuniv.munchmap.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;


@Tag(name = "UserController", description = "User 관련 기능 | 회원가입, 로그인, 마이페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

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
            @Valid @RequestBody UserRequest.userFavor userFavor,
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
}
