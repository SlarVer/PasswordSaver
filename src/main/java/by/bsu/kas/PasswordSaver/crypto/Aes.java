package by.bsu.kas.PasswordSaver.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Aes {
    public static String encrypt(String input, String stringKey) {
        try {
            //Getting key from the string
            byte[] decodedKey = Base64.getDecoder().decode(stringKey);
            SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            //Encryption
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypt = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String input, String stringKey) {
        try {
            //Getting key from the string
            byte[] decodedKey = Base64.getDecoder().decode(stringKey);
            SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            //Decryption
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedInput = Base64.getDecoder().decode(input);
            byte[] decrypt = cipher.doFinal(decodedInput);
            return new String(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
