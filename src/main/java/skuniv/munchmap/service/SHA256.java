package skuniv.munchmap.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SHA256 {
    private static final int[] INITIAL_HASH_VALUES = {
            0x6a09e667,
            0xbb67ae85,
            0x3c6ef372,
            0xa54ff53a,
            0x510e527f,
            0x9b05688c,
            0x1f83d9ab,
            0x5be0cd19
    };

    public int[] hashvalues;

    public int[] hashClone() {
        hashvalues = INITIAL_HASH_VALUES.clone();   // 초기화 - 해시 값을 복사해서 사용할 준비.
        return hashvalues;
    }

    public void printhashvalues() {
        for (int value : hashvalues) {
            System.out.printf("%08x ", value);
        }
        System.out.println();
    }

    public byte[] pedMessage(byte[] Message) {
        int originLength = Message.length;
        int totalLength = ((originLength + 8) / 64 + 1) * 64;    // 메시지 길이를 512비트(64바이트) 블록 단위로 맞추기 위한 작업
        byte[] paddedMessage = new byte[totalLength];

        System.arraycopy(Message, 0, paddedMessage, 0, originLength);   // 512 맞춰주기 위해 원래 메시지를 복사
        paddedMessage[originLength] = (byte) 0x80;  //1을 추가하고 나머지를 0으로 채우는 작업

        long messageBits = (long) originLength * 8;     // 자바에서 int 타입은 32비트이기 때문에 long 타입으로 함.
        for (int i = 0; i < 8; i++) {
            paddedMessage[totalLength - 1 - i] = (byte) (messageBits >>> (8 * i));
        }
        return paddedMessage;
    }

    public byte[][] blockMessages(byte[] Message) {
        int blockSize = 64;
        byte[] peddedMessage = pedMessage(Message);
        int numBlock = peddedMessage.length / blockSize;
        byte[][] blocks = new byte[numBlock][blockSize]; // 메시지가 길 경우를 생각해서 이차원 배열로 설정해야함. (처음엔 일차원으로만 했었음)

        for (int i = 0; i < numBlock; i++) {
            System.arraycopy(pedMessage(Message), i * blockSize, blocks[i], 0, blockSize);
        }

        return blocks;
    }

    public int[] messageSchedule(byte[] blockMessage) {
        int[] W = new int[64];

        for (int i = 0; i < 16; i++) {
            W[i] = ((blockMessage[i * 4] & 0xFF) << 24) |
                    ((blockMessage[i * 4 + 1] & 0xFF) << 16) |
                    ((blockMessage[i * 4 + 2] & 0xFF) << 8) |
                    (blockMessage[i * 4 + 3] & 0xFF);
        }

        for(int i =16; i < 64; i++) {
            W[i] = sigma1(W[i - 2]) + W[i - 7] + sigma0(W[i - 15]) + W[i - 16];
        }

        return W;
    }

        int[] initial = INITIAL_HASH_VALUES.clone(); // 초기 해시 값을 복사하여 사용 준비

        int a = initial[0];
        int b = initial[1];
        int c = initial[2];
        int d = initial[3];
        int e = initial[4];
        int f = initial[5];
        int g = initial[6];
        int h = initial[7];

    private int sigma0(int x) {
        return (Integer.rotateRight(x, 7)) ^ (Integer.rotateRight(x, 18)) ^ (x >>> 3);
    }

    private int sigma1(int x) {
        return (Integer.rotateRight(x, 17)) ^ (Integer.rotateRight(x, 19)) ^ (x >>> 10);
    }

    // Σ1(e) 함수
    private int bigSigma1(int e) {
        return Integer.rotateRight(e, 6) ^ Integer.rotateRight(e, 11) ^ Integer.rotateRight(e, 25);
    }

    // Ch(e, f, g) 함수
    private int Ch(int e, int f, int g) {
        return (e & f) ^ ((~e) & g);
    }

    private int bigSigma0(int a) {
        return Integer.rotateRight(a, 2) ^ Integer.rotateRight(a, 13) ^ Integer.rotateRight(a, 22);
    }

    private int Maj(int a, int b, int c) {
        return (a & b) ^ (a & c) ^ (b & c);
    }

    public void shortprocess(byte[] Message) {
        int T1 = 0;
        int T2 = 0;

        int[] K = {
                0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5,
                0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
                0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3,
                0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
                0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc,
                0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
                0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
                0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
                0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,
                0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
                0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3,
                0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
                0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5,
                0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
                0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,
                0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
        };

        int[] W;
        W = messageSchedule(Message);

        for (int i = 0; i < 64; i++) {
            T1 = h + bigSigma1(e) + Ch(e, f, g) + K[i] + W[i];
            T2 = bigSigma0(a) + Maj(a, b, c);


            h = g;
            g = f;
            f = e;
            e = d + T1;
            d = c;
            c = b;
            b = a;
            a = T1 + T2;
        }
    }

    private void updatehash(int[] initial) {
        initial[0] += a;
        initial[1] += b;
        initial[2] += c;
        initial[3] += d;
        initial[4] += e;
        initial[5] += f;
        initial[6] += g;
        initial[7] += h;
    }

    private void initialhash(int[] initial) {
        a = initial[0];
        b = initial[1];
        c = initial[2];
        d = initial[3];
        e = initial[4];
        f = initial[5];
        g = initial[6];
        h = initial[7];
    }

//    private byte[] convertToByte(int[] hash) {
//        byte[] hashBytes = new byte[32]; // 256비트를 표현하기 위해 32바이트 배열 생성
//
//        System.out.println("Final Hash: " + Arrays.toString(hash));
//
//        for (int i = 0; i < hash.length; i++) {
//            hashBytes[i * 4] = (byte) ((hash[i] >> 24) & 0xFF);    // 상위 8비트
//            hashBytes[i * 4 + 1] = (byte) ((hash[i] >> 16) & 0xFF); // 중상위 8비트
//            hashBytes[i * 4 + 2] = (byte) ((hash[i] >> 8) & 0xFF);  // 중하위 8비트
//            hashBytes[i * 4 + 3] = (byte) (hash[i] & 0xFF);         // 하위 8비트
//        }
//
//        return hashBytes;
//    }

    private byte[] convertToByte(int[] hash) {
        byte[] hashBytes = new byte[32]; // SHA-256의 해시 값은 32바이트임.

        // Final Hash 값을 byte[]로 변환하는 과정에서 오류가 발생할 수 있음
        for (int i = 0; i < hash.length; i++) {
            hashBytes[i * 4] = (byte) ((hash[i] >> 24) & 0xFF);
            hashBytes[i * 4 + 1] = (byte) ((hash[i] >> 16) & 0xFF);
            hashBytes[i * 4 + 2] = (byte) ((hash[i] >> 8) & 0xFF);
            hashBytes[i * 4 + 3] = (byte) (hash[i] & 0xFF);
        }

        // byte[]로 변환된 값을 제대로 출력하는지 확인
        System.out.println("Converted Hash Bytes: " + Arrays.toString(hashBytes));

        return hashBytes;
    }




    public byte[] SHA256(byte[] message) {
        // 1. 메시지를 패딩하고 블록으로 분할
        byte[][] blocks = blockMessages(message);

        // 2. 초기 해시 값 설정
        int[] hash = hashClone();

        // 3. 각 블록에 대해 shortprocess 수행
        for (byte[] block : blocks) {
            initialhash(hash);  // 초기화된 해시 값으로 a~h 설정
            shortprocess(block); // shortprocess에서 메시지 처리
            updatehash(hash);    // shortprocess 결과를 hash에 반영
        }

        // 4. 최종 해시 값을 바이트 배열로 변환
        return convertToByte(hash);
    }

}