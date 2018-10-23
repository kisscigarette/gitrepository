package com.kisscigarette.app.common;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Title:AESUtil
 * Description:AES加密工具类
 * @sine 2017-6-2上午9:56:44
 * @author yowasa
 */
public class AESUtil {
	public static final String UTF_8 = "UTF-8";
    public static final String AES_ALGORITHM = "AES/CFB/PKCS5Padding";
	/**
	 * 获取随机的对称加密AES128位Base64编码的的密钥
	 * @author yowasa
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 */
    public static String genRandomAesSecretKey()  throws NoSuchAlgorithmException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException{
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        String keyWithBase64 = Base64.encodeToString(secretKey.getEncoded(),Base64.DEFAULT);
        return keyWithBase64;
    }
    /**
     * 获取随机的对称加密AES的IV偏移值并以Base64编码返回
     * @author yowasa
     * @return
     */
    public static String genRandomIV() throws NoSuchAlgorithmException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException{
        SecureRandom r = new SecureRandom();
        byte[] iv = new byte[16];
        r.nextBytes(iv);
        String ivParam = Base64.encodeToString(iv,Base64.DEFAULT);
        return ivParam;
    }
    /**
     * 对称加密数据
     * @author yowasa
     * @param keyWithBase64
     * @param ivWithBase64
     * @param plainText
     * @param aesMod
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     */
    public static String encryptAES(String keyWithBase64, String ivWithBase64, String plainText,String aesMod)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        
        SecretKeySpec key = new SecretKeySpec(Base64.decode(keyWithBase64,Base64.DEFAULT), "AES");
        IvParameterSpec iv = new IvParameterSpec(Base64.decode(ivWithBase64,Base64.DEFAULT));
        
        Cipher cipher = Cipher.getInstance(aesMod);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
       
        return Base64.encodeToString(cipher.doFinal(plainText.getBytes(UTF_8)),Base64.DEFAULT);
    }
    /**
     * 对称解密数据
     * @author yowasa
     * @param keyWithBase64
     * @param ivWithBase64
     * @param cipherText
     * @param aesMod
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     */
    public static String decryptAES(String keyWithBase64, String ivWithBase64, String cipherText,String aesMod)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        SecretKeySpec key = new SecretKeySpec(Base64.decode(keyWithBase64,Base64.DEFAULT), "AES");
        IvParameterSpec iv = new IvParameterSpec(Base64.decode(ivWithBase64,Base64.DEFAULT));
        
        Cipher cipher = Cipher.getInstance(aesMod);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(Base64.decode(cipherText,Base64.DEFAULT)), UTF_8);
    }
    
    
}
