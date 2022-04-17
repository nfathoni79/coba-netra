package id.nfathoni.cobanetra.util;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {

    private static final String TAG = "AesUtil";
    private static final int IV_LENGTH = 16;
    private static IvParameterSpec ivParam;

    private static SecretKeySpec createSecretKey(String keyString) {
        try {
            byte[] key = keyString.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setIvParam(Cipher cipher) {
//        SecureRandom rnd = new SecureRandom();
//        byte[] iv = new byte[cipher.getBlockSize()];
//        rnd.nextBytes(iv);
//        ivParam = new IvParameterSpec(iv);

        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        ivParam = new IvParameterSpec(iv);

//        ivParam = new IvParameterSpec(IV_STRING.getBytes(StandardCharsets.UTF_8));
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String generateIvB64() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return Base64.encodeToString(iv, Base64.DEFAULT);
    }

    public static IvParameterSpec createIv(String ivB64) {
        byte[] iv = Base64.decode(ivB64, Base64.DEFAULT);
        return new IvParameterSpec(iv);
    }

    public static String cbcEncrypt(String strToEncrypt, String secret, String ivB64) {
        try {
            SecretKeySpec secretKey = createSecretKey(secret);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            IvParameterSpec iv = createIv(ivB64);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "Error while encrypting: " + e);
        }

        return null;
    }

    public static String cbcEncrypt(String strToEncrypt, String secret, IvParameterSpec iv) {
        try {
            SecretKeySpec secretKey = createSecretKey(secret);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "Error while encrypting: " + e);
        }

        return null;
    }

    public static String ecbEncrypt(String plainText, String secret) {
        try {
            SecretKeySpec secretKey = createSecretKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return Base64.encodeToString(cipherText, Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e(TAG, "Error while encrypting: " + e);
        }

        return null;
    }

    public static String cbcDecrypt(String strToDecrypt, String secret, String ivB64) {
        try {
            SecretKeySpec secretKey = createSecretKey(secret);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

            IvParameterSpec iv = createIv(ivB64);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            return new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)));
        } catch (Exception e) {
            Log.e(TAG, "Error while decrypting: " + e);
        }

        return null;
    }

    public static String cbcDecrypt(String strToDecrypt, String secret, IvParameterSpec iv) {
        try {
            SecretKeySpec secretKey = createSecretKey(secret);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            return new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)));
        } catch (Exception e) {
            Log.e(TAG, "Error while decrypting: " + e);
        }

        return null;
    }

    public static String ecbDecrypt(String cipherText, String secret) {
        try {
            SecretKeySpec secretKey = createSecretKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] plainText = cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT));

            return new String(plainText);
        } catch (Exception e) {
            Log.e(TAG, "Error while decrypting: " + e);
        }

        return null;
    }
}
