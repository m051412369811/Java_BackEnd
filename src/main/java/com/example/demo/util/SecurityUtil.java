package com.example.demo.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public final class SecurityUtil {
    private SecurityUtil() {
    }

    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(saltBytes);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashedPassword);
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
