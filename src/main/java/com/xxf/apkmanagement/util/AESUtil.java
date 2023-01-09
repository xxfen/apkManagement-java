package com.xxf.apkmanagement.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;

public class AESUtil {

    protected final static Log logger = LogFactory.getLog(AESUtil.class);
    // 算法名称
    final static String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    final static String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    // 字符集
    final static String CHARSET_NAME = "UTF-8";
    final static String key = "xxf89dshfsfhsbj";
    // 填充矢量
    final static byte[] iv = {0x30, 0x31, 0x30, 0x32, 0x30, 0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37, 0x30, 0x38};
    //final static byte[] iv = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    private static Key getKey(byte[] keyBytes) {
        // 如果密钥不足16位，那么就补足
        int base = 16;

        if ((keyBytes.length % base) != 0) {
            int groups = (keyBytes.length / base) + ((keyBytes.length % base) != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }

        // 转化成JAVA的密钥格式
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    /**
     * 加密方法
     *
     * @param content 要加密的字符串
     * @param key 加密密钥
     * @return
     */
    public static String encrypt(String content, String key) {
        try {
            byte[] enc = encrypt(content.getBytes(CHARSET_NAME), key.getBytes(CHARSET_NAME));
            String base64Str = Base64.encodeBase64String(enc);
            return new String(base64Str.getBytes(), CHARSET_NAME);
        }
        catch (UnsupportedEncodingException e) {
            logger.error(" 加密密钥异常 " , e);
        }

        return null;
    }

    /**
     * 加密
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        return encrypt(content,key);
    }

    /**
     * 加密方法
     *
     * @param content 要加密的字符串
     * @param keyBytes 加密密钥
     * @return
     */
    public static byte[] encrypt(byte[] content, byte[] keyBytes) {
        byte[] encryptedText = null;

        try {
            Key key = getKey(keyBytes);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            encryptedText = cipher.doFinal(content);
        }
        catch (Exception e) {
            logger.error(" 加密密钥异常 " , e);
        }
        return encryptedText;
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param key 解密密钥
     * @return
     */
    public static String decrypt(String encryptedData, String key) {
        try {
            byte[] enc = encryptedData.getBytes(CHARSET_NAME);
            enc = Base64.decodeBase64(enc);
            byte[] dec = decrypt(enc, key.getBytes(CHARSET_NAME));
            return new String(dec, CHARSET_NAME);
        }
        catch (UnsupportedEncodingException e) {
            logger.error(" 解密密钥异常 " , e);
        }

        return null;
    }

    /**
     * 解密
     * @param encryptedData
     * @return
     */
    public static String decrypt(String encryptedData ) {
        return decrypt(encryptedData,key);
    }
    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param keyBytes 解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes) {
        byte[] encryptedText = null;

        try {
            Key key = getKey(keyBytes);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            encryptedText = cipher.doFinal(encryptedData);
        }
        catch (Exception e) {
            logger.error(" 解密密钥异常 " , e);
        }

        return encryptedText;
    }
/*
    public static void main(String[] args) throws Exception {

        // 加解密 密钥
//        String key = "83519aa6d30ecdc3";// "paic1234";
        long timestamp = new Date().getTime();
        long nonce = new Random().nextInt(100000);
        String toSign = timestamp+""+nonce;
        System.out.println("signature：" + toSign);
//        String signature = HmacSHA1Utils.getSignature(toSign, key);
        String content = "1";
        // 加密字符串
        System.out.println("加密前的：" + content);
        System.out.println("加密密钥：" + key);
        // 加密方法
        String enc = AESUtil.encrypt(content, key);
        System.out.println("加密后的内容：" + enc);

        String encode = URLEncoder.encode(enc, "UTF-8");
        System.out.println("编码后的内容：" + encode);

        //URL encode
        enc = URLEncoder.encode(enc, CHARSET_NAME);
        System.out.println("URL encode：" + enc);
        //URL decode
        String dec = URLDecoder.decode(enc, CHARSET_NAME);
        System.out.println("URL decode：" + dec);
        // 解密方法
        dec = AESUtil.decrypt(dec, key);
        System.out.println("解密后的内容：" + dec);
    }*/
}