package skuniv.munchmap.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Arrays;

@Component
public class Salt {

    // 비밀번호와 무관하게 그냥 랜덤하게 salt를 만듬.
    public byte[] randomsalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        System.out.println("Generated Salt: " + Arrays.toString(salt));

        return salt;
    }

    public byte[] salt(byte[] salt, byte[] password) {
        byte[] combined = new byte[salt.length + password.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(password, 0, combined, salt.length, password.length);

        System.out.println("Salted Password: " + Arrays.toString(combined));

        return combined;
    }

}
