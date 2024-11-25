package skuniv.munchmap.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import skuniv.munchmap.config.exception.BadRequestException;
import skuniv.munchmap.config.exception.ErrorResponseStatus;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PasswordEncoder {

    // 초기화 벡터
    private static final int[] K = new int[64];
    private static final int[] H = new int[8];

    static {
        // K 값 초기화: 첫 32개 소수의 세제곱근의 소수부 첫 32비트
        int[] primes = generatePrimes(311); // 64번째 소수까지 생성
        for (int i = 0; i < 64; i++) {
            K[i] = (int)(getFractionalPart(Math.cbrt(primes[i])) * (1L << 32));
        }

        // H 값 초기화: 첫 8개 소수의 제곱근의 소수부 첫 32비트
        for (int i = 0; i < 8; i++) {
            H[i] = (int)(getFractionalPart(Math.sqrt(primes[i])) * (1L << 32));
        }
    }

    // 소수 생성 메소드
    private static int[] generatePrimes(int max) {
        List<Integer> primes = new ArrayList<>();
        boolean[] isComposite = new boolean[max + 1];

        for (int i = 2; i <= max; i++) {
            if (!isComposite[i]) {
                primes.add(i);
                for (int j = i * i; j <= max; j += i) {
                    isComposite[j] = true;
                }
            }
        }

        return primes.stream().mapToInt(Integer::intValue).toArray();
    }

    // 소수부 추출 메소드
    private static double getFractionalPart(double number) {
        return number - Math.floor(number);
    }

    // Salt 생성
    public String generateSalt() {
        SecureRandom r = new SecureRandom();
        byte[] salt = new byte[20];
        r.nextBytes(salt);
        return bytesToHex(salt);
    }

    // 최종 salt와 더하는 암호화 코드
    public static String encrypt(String password, String salt) {
//        if (password == null || salt == null) {
//            throw new BadRequestException(ErrorResponseStatus.INVALID_PWD_SALT);
//        }

        String input = password + salt;
        byte[] bytes = input.getBytes();

        // 전처리: 패딩
        int originalLength = bytes.length * 8;
        int paddingLength = (448 - (originalLength + 1)) % 512;
        if (paddingLength < 0) paddingLength += 512;

        byte[] paddedInput = new byte[bytes.length + (paddingLength + 65) / 8];
        System.arraycopy(bytes, 0, paddedInput, 0, bytes.length);
        paddedInput[bytes.length] = (byte) 0x80;

        // 메시지 길이를 비트 단위로 마지막 64비트에 추가
        for (int i = 0; i < 8; i++) {
            paddedInput[paddedInput.length - 8 + i] = (byte) (originalLength >>> (56 - 8 * i));
        }

        // 512비트(64바이트) 블록으로 처리
        int[] state = H.clone();
        for (int i = 0; i < paddedInput.length; i += 64) {
            processBlock(paddedInput, i, state);
        }

        // 최종 해시값을 바이트 배열로 변환
        byte[] hash = new byte[32];
        for (int i = 0; i < 8; i++) {
            hash[i * 4] = (byte) (state[i] >>> 24);
            hash[i * 4 + 1] = (byte) (state[i] >>> 16);
            hash[i * 4 + 2] = (byte) (state[i] >>> 8);
            hash[i * 4 + 3] = (byte) state[i];
        }

        return bytesToHex(hash);
    }

    private static void processBlock(byte[] data, int offset, int[] state) {
        int[] w = new int[64];

        // 메시지 스케줄 준비
        for (int i = 0; i < 16; i++) {
            w[i] = ((data[offset + 4 * i] & 0xFF) << 24) |
                    ((data[offset + 4 * i + 1] & 0xFF) << 16) |
                    ((data[offset + 4 * i + 2] & 0xFF) << 8) |
                    (data[offset + 4 * i + 3] & 0xFF);
        }

        for (int i = 16; i < 64; i++) {
            int s0 = rotateRight(w[i - 15], 7) ^ rotateRight(w[i - 15], 18) ^ (w[i - 15] >>> 3);
            int s1 = rotateRight(w[i - 2], 17) ^ rotateRight(w[i - 2], 19) ^ (w[i - 2] >>> 10);
            w[i] = w[i - 16] + s0 + w[i - 7] + s1;
        }

        // 작업 변수 초기화를 배열로 처리
        int[] v = new int[8];
        System.arraycopy(state, 0, v, 0, 8);

        // 메인 루프
        for (int i = 0; i < 64; i++) {
            int s1 = rotateRight(v[4], 6) ^ rotateRight(v[4], 11) ^ rotateRight(v[4], 25);
            int ch = (v[4] & v[5]) ^ (~v[4] & v[6]);
            int temp1 = v[7] + s1 + ch + K[i] + w[i];
            int s0 = rotateRight(v[0], 2) ^ rotateRight(v[0], 13) ^ rotateRight(v[0], 22);
            int maj = (v[0] & v[1]) ^ (v[0] & v[2]) ^ (v[1] & v[2]);
            int temp2 = s0 + maj;

            // 작업 변수 순환
            v[7] = v[6];
            v[6] = v[5];
            v[5] = v[4];
            v[4] = v[3] + temp1;
            v[3] = v[2];
            v[2] = v[1];
            v[1] = v[0];
            v[0] = temp1 + temp2;
        }

        // 최종 해시값 업데이트를 반복문으로 처리
        for (int i = 0; i < 8; i++) {
            state[i] += v[i];
        }
    }

    private static int rotateRight(int value, int distance) {
        return (value >>> distance) | (value << (32 - distance));
    }

    // 16진수 문자열 반환
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 로그인시 비밀번호 검증
    public static boolean verifyPassword(String password, String salt, String bytesToHex) {
        String newHashedPassword = encrypt(password, salt);
        return newHashedPassword.equals(bytesToHex);
    }
}