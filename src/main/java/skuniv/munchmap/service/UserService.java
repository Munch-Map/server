package skuniv.munchmap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skuniv.munchmap.domain.User;
import skuniv.munchmap.dto.UserRequestDTO;
import skuniv.munchmap.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;


@Service
@Transactional
public class UserService {

    @Autowired
    public UserService(UserRepository userRepository, SHA256 sha256, Salt salt) {
        this.userRepository = userRepository;
        this.sha256 = sha256;
        this.salt = salt;
    }

    private final UserRepository userRepository;
    private final SHA256 sha256;
    private final Salt salt;


    // 아이디 중복
    public boolean isLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public String securepassword(String rawPassword) {
        // 1. 랜덤 salt 생성
        byte[] generatedSalt = salt.randomsalt();
        System.out.println("Generated Salt Length: " + generatedSalt.length);

        // 2. Salt와 비밀번호 결합 -> 비밀번호 + salt 순으로 함
        byte[] saltedPassword = salt.salt(rawPassword.getBytes(StandardCharsets.UTF_8), generatedSalt);

        // 3. SHA-256 해시 계산
        byte[] hashedPassword = sha256.SHA256(saltedPassword);

        // 4. Salt와 해시 결합 후 Base64로 인코딩
        byte[] combined = new byte[generatedSalt.length + hashedPassword.length];
        System.arraycopy(generatedSalt, 0, combined, 0, generatedSalt.length);
        System.arraycopy(hashedPassword, 0, combined, generatedSalt.length, hashedPassword.length);

        return encodeToBase64(combined); // encodeToBase64 호출
    }

    // 비밀번호 동일 확인
    public boolean match(String rawPassword, String encryptedPasswordBase64) {
        try {
            // 저장된 암호화된 비밀번호를 Base64로 디코딩하여 바이트 배열로 변환
            byte[] encryptedPasswordBytes = Base64.getDecoder().decode(encryptedPasswordBase64);
            System.out.println("디코딩된 암호화된 비밀번호 바이트 배열 길이: " + encryptedPasswordBytes.length);

            // 저장된 비밀번호에서 salt 추출 (앞부분에 위치한 salt 길이를 알고 있어야 함)
            int saltLength = 16; // 예를 들어, salt 길이가 16바이트라고 가정
            byte[] extractedSalt = Arrays.copyOfRange(encryptedPasswordBytes, 0, saltLength);
            System.out.println("추출된 salt: " + bytesToHex(extractedSalt));

            // 저장된 해시된 비밀번호 추출
            byte[] storedHashedPassword = Arrays.copyOfRange(encryptedPasswordBytes, saltLength, encryptedPasswordBytes.length);
            System.out.println("저장된 해시된 비밀번호: " + bytesToHex(storedHashedPassword));

            // 입력된 비밀번호와 추출한 salt를 결합하여 해시 계산
            byte[] saltedPassword = salt.salt(rawPassword.getBytes(), extractedSalt);
            System.out.println("saltedPassword: " + bytesToHex(saltedPassword));

            byte[] hashedPassword = sha256.SHA256(saltedPassword);
            System.out.println("입력된 비밀번호의 해시값: " + bytesToHex(hashedPassword));

            // 해시된 비밀번호가 저장된 해시된 비밀번호와 일치하는지 비교
            boolean isMatch = Arrays.equals(hashedPassword, storedHashedPassword);
            System.out.println("비밀번호 일치 여부: " + isMatch);
            return isMatch;
        } catch (Exception e) {
            System.out.println("비밀번호 비교 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 바이트 배열을 16진수 문자열로 변환하는 헬퍼 메소드
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    public String encodeToBase64(byte[] data) {
        String encoded = Base64.getEncoder().encodeToString(data);
        // Base64 인코딩 출력
        System.out.println("Base64 Encoded Value: " + encoded);
        return encoded;
    }

    private byte[] extractSalt(String encryptedPassword) {
        byte[] decoded = Base64.getDecoder().decode(encryptedPassword);
        System.out.println("Decoded Length: " + decoded.length); // 디코딩된 배열 길이 확인
        byte[] extractedSalt = new byte[16]; // Salt는 16바이트로 고정
        System.arraycopy(decoded, 0, extractedSalt, 0, 16);
        System.out.println("Extracted Salt: " + Arrays.toString(extractedSalt)); // 추출된 salt 확인
        return extractedSalt;
    }

    // 회원가입
    @Transactional
    public void signup(UserRequestDTO.userInfo userInforequest) {
        User user = User.builder()
                .loginId(userInforequest.getLoginId())
              .password(securepassword(userInforequest.getPassword()))
                .name(userInforequest.getName())
                .email(userInforequest.getEmail())
                .address(userInforequest.getCity() + " " + userInforequest.getDistrict() + " " + userInforequest.getAddressDetail())
                .favor(userInforequest.getFavor())
                .build();

        userRepository.save(user);
    }

    // 아아디로 사용자 찾기
    public User getById(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login_id: " + loginId));
    }
}
