package com.xxf.apkmanagement.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {
    private final static String KEY_RSA = "RSA";
    private final static int RSA_INIT_LENGTH = 1024;

    /**
     * 初始化
     */
    public static String[] initRSAKey() {
        String[] keys = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_RSA);
            keyPairGenerator.initialize(RSA_INIT_LENGTH);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            // 公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            keys = new String[]{encryptBASE64(publicKey.getEncoded()), encryptBASE64(privateKey.getEncoded())};
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keys;
    }

    /**
     * 私钥加密
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) {
        try {
            byte[] keyBytes = decryptBASE64(key);
            // 获得私钥
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
            Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥解密
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) {
        try {
            // 对私钥解密
            byte[] keyBytes = decryptBASE64(key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
            Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥加密
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) {
        try {
            // 对公钥解密
            byte[] keyBytes = decryptBASE64(key);
            // 取公钥
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
            Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 私钥解密
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        try {
            // 对私钥解密
            byte[] keyBytes = decryptBASE64(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA);
            Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     */
    public static String encryptBASE64(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }


    /**
     * 解密
     */
    public static byte[] decryptBASE64(String cipher) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = null;
        try {
            bytes = decoder.decodeBuffer(cipher);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }


}
