package skuniv.munchmap.service;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import skuniv.munchmap.config.exception.BadRequestException;
import skuniv.munchmap.config.exception.ErrorResponseStatus;
import skuniv.munchmap.domain.Category;
import skuniv.munchmap.domain.User;
import skuniv.munchmap.domain.UserFavorCategory;
import skuniv.munchmap.dto.StoreResponse;
import skuniv.munchmap.repository.CategoryRepository;
import skuniv.munchmap.repository.StoreRepository;
import skuniv.munchmap.repository.UserFavorCategoryRepository;
import skuniv.munchmap.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skuniv.munchmap.dto.UserRequest;
import skuniv.munchmap.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final UserFavorCategoryRepository userFavorCategoryRepository;

    // (1) 회원가입 메서드
    @Transactional
    public User registerUser(UserRequest.userInfo userInfo) {
        // 중복 아이디, 이메일 검증
        validateDuplicateUser(userInfo);

        // 비밀번호 유효성 검증
        validatePassword(userInfo.getPassword());

        // 비밀번호 암호화
        String salt = passwordEncoder.generateSalt();
        String hashedPassword = passwordEncoder.encrypt(userInfo.getPassword(), salt);

        User user = User.builder()
                .loginId(userInfo.getLogin_id())
                .password(hashedPassword)
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .address(userInfo.getAddress())
                .salt(salt)
                .build();

        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause != null) {
                cause.printStackTrace();
            }
            throw new RuntimeException("Unexpected error occurred", e);
        }

        return savedUser;
    }

    // (1)-1 아이디와 이메일 유효성 검사 메서드
    private void validateDuplicateUser(UserRequest.userInfo userInfo) {
        // 아이디가 이미 존재하거나 입력하지 않은 경우 에러처리
        if (userRepository.existsByLoginId(userInfo.getLogin_id()) || userInfo.getLogin_id() == null) {
            throw new BadRequestException(ErrorResponseStatus.INVALID_USERID);
        }

        // 이메일이 이미 존재하면 에러처리
        if (userRepository.existsByEmail(userInfo.getEmail())) {
            throw new BadRequestException(ErrorResponseStatus.DUPLICATE_EMAIL);
        }
    }

    // (1)-2 비밀번호 유효성 검사
    private void validatePassword(String password) {
        //비밀번호를 입력하지 않았거나 8글자 이하인 경우 에러처리
        if (password == null || password.length() < 8) {
            throw new BadRequestException(ErrorResponseStatus.INVALID_PWD);
        }
        // 비밀번호 조합 시, 조건을 갖추지 않으면 에러처리
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
            throw new BadRequestException(ErrorResponseStatus.INVALID_PWD_FORMAT);
        }
    }

    // (2) 사용자 선호 카테고리 선택 메서드
    @Transactional
    public List<String> chooseUserFavor(UserRequest.userFavor userFavor, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ErrorResponseStatus.INVALID_USERID));

        List<Category> categories = StreamSupport.stream(
                categoryRepository.findAllById(userFavor.getCategoryIds()).spliterator(),
                false
        ).collect(Collectors.toList());

        // 카테고리 설정
        List<UserFavorCategory> userFavorCategories = categories.stream()
                .map(category -> UserFavorCategory.builder()
                        .user(user)
                        .category(category)
                        .build())
                .collect(Collectors.toList());
        userFavorCategoryRepository.saveAll(userFavorCategories);

        // 저장된 카테고리 이름 반환
        return categories.stream()
                .map(Category::getType)
                .collect(Collectors.toList());
    }


    // (3) 로그인 메서드
    @Transactional
    public void login(UserRequest.loginRequestDTO loginRequest, HttpSession session) {
        // 사용자 확인
        User user = userRepository.findByLoginId(loginRequest.getLogin_id())
                .orElseThrow(() -> new BadRequestException(ErrorResponseStatus.INVALID_USERID));

        // 비밀번호 검증
        boolean isValidPassword = PasswordEncoder.verifyPassword(
                loginRequest.getPassword(), user.getSalt(), user.getPassword());

        if (!isValidPassword) {
            throw new BadRequestException(ErrorResponseStatus.NOT_EXIST_PW);
        }

        // 세션에 사용자 정보 저장
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getName());

        // 응답 반환
        ResponseEntity.ok().build();
    }

    // (4) 로그아웃 메서드
    @Transactional
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok().build();
    }
}