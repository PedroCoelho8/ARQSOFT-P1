package pt.psoft.g1.psoftg1.idgeneratormanagement;

import java.security.SecureRandom;

public class IdGeneratorAlphanumeric implements IdGenerator {
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 20;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateId() {
        StringBuilder idBuilder = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_CHARACTERS.length());
            idBuilder.append(ALPHANUMERIC_CHARACTERS.charAt(randomIndex));
        }
        return idBuilder.toString();
    }
}