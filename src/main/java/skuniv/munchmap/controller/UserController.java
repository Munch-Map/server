package skuniv.munchmap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skuniv.munchmap.config.exception.InternalServerErrorException;
import skuniv.munchmap.config.exception.NotFoundException;
import skuniv.munchmap.config.exception.UnauthorizedException;
import skuniv.munchmap.domain.User;
import skuniv.munchmap.dto.UserRequestDTO;
import skuniv.munchmap.service.UserService;

@Tag(name = "UserController(사용자 API)")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @GetMapping("/check-loginId")
    @Operation(summary = "아이디 중복 확인", description = "아이디 중복 확인")
    public ResponseEntity<String> checkLoginId(@RequestParam String loginId) {
        if (userService.isLoginIdDuplicate(loginId)) {
            return ResponseEntity.badRequest().body("Login ID already exists");
        } else {
            return ResponseEntity.ok("Login ID is available");
        }
    }

    // Encoder
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입")
    public ResponseEntity<String> signup(@RequestBody UserRequestDTO.userInfo userInfoRequest) {
        System.out.println("signup start");

        // 비밀번호 암호화
        String password = userService.securepassword(userInfoRequest.getPassword());
        System.out.println("Encoded Password (during signup): " + password);

        userService.signup(userInfoRequest);

        // 회원가입 후 바로 로그인이 되도록 함.
        User user = userService.getById(userInfoRequest.getLoginId());

        if (user == null) {
            throw new UnauthorizedException("No user in database");
        }

        // 응답 반환 - 이름만 반환
        return ResponseEntity.ok(user.getName());
    }

    // 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인")
    public ResponseEntity<String> login(@RequestBody UserRequestDTO.login loginRequest, HttpSession session) {
        try {
            User user = userService.getById(loginRequest.getLoginId());

            // 회원정보가 있는 경우
            if (user != null) {
                if (userService.match(loginRequest.getPassword(), user.getPassword())) {
                    session.setAttribute("user", user);
                    return ResponseEntity.ok(user.getName());
                } else {
                    throw new UnauthorizedException("Invalid credentials");
                }
            } else {
                throw new NotFoundException("Invalid loginId");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal Server Error");
        }
    }
}
