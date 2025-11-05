package com.radieske.reservasapi.util;

import org.springframework.security.crypto.codec.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Decrypt ticket and encrypt temp password
 *
 */
public class PasswordEncryptUtil {

    /**
     *   When JAVA, PKCS7Padding was named "PKCS5Padding"
     */
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String AES = "AES";

    /**
     *
     * @param plainTextPassword plain text password
     * @param accessSecret your Access Secret
     * @param ticketKey get from POST /v1.0/devices/{device_id}/door-lock/password-ticket
     * @return encrypt password
     * @throws Exception
     */
    public static String encryptPassword(String plainTextPassword, String accessSecret, String ticketKey) throws Exception {
        String plainTextTicketKey = decrypt(ticketKey, accessSecret);
        return encrypt(plainTextPassword, plainTextTicketKey);
    }

    private static String encrypt(String plainText, String secretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), AES);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(encryptedBytes));
    }

    private static String decrypt(String encryptedText, String secretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), AES);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Hex.decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}

