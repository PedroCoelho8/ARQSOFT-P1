package pt.psoft.g1.psoftg1.idgeneratormanagement;

import java.security.SecureRandom;

public class IdGeneratorHexadecimal implements IdGenerator{
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateId() {
        byte[] randomBytes = new byte[12]; // 12 bytes * 2 = 24 hexadecimal characters
        secureRandom.nextBytes(randomBytes);
        return bytesToHex(randomBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
