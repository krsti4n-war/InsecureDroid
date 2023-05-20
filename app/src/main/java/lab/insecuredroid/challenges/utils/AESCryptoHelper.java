package lab.insecuredroid.challenges.utils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCryptoHelper {

    static final String base64Key = "NXVwM1JzM2NSMzczbmNSeVA3MTBuazN5cjFnSHQ/Pz8=";
    static final String base64IV = "AQIDBAUGBwgJCgsMDQ4PEA==";

    public static String encrypt(String secret) {

        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            byte[] iv = Base64.getDecoder().decode(base64IV);
            SecretKey secretKeySpeck = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpeck, new IvParameterSpec(iv));
            byte[] plaintextBytes = cipher.doFinal(secret.getBytes());
            return Base64.getEncoder().encodeToString(plaintextBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            byte[] iv = Base64.getDecoder().decode(base64IV);
            SecretKey secretKeySpeck = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpeck, new IvParameterSpec(iv));
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
