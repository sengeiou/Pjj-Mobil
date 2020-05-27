package com.pjj.utils.aes;

import android.util.Log;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author：xinheng
 * Description：aes加密解密
 * Time: 2018/4/8
 */
public class AESUtils {

//    private static final String key = "7mm:rYRnadsr1r1o";
//    private static final String iv = "7mm:rYRnadsr1r1o";
    private static final String key = "pjjky";
    private static final String iv = "pjjky";
    public static String encrypt(String data) {
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//            Cipher cipher = Cipher.getInstance(CipherMode);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            Log.e("TAG", "encrypt: "+blockSize+"， "+plaintextLength );
            boolean num16 = blockSize > 0 && plaintextLength % blockSize != 0;//不能被16整除
            if (num16) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
                //byte b = " ".getBytes()[1];
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            if(num16){
                for (int i=dataBytes.length;i<plaintextLength;i++){
                    plaintext[i]= 32;
                }
            }
            byte[] bytes = key.getBytes();
            SecretKeySpec keyspec = new SecretKeySpec(bytes, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            //byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
            //return Base64.encode(encrypted);
//            return byte2hex(encrypted);
            return new BASE64Encoder().encode(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "encrypt: 加密失败");
            return null;
        }
    }
    /** 创建密钥 **/
    private static SecretKeySpec createKey(String key) {
        byte[] data = null;
        if (key == null) {
            key = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(key);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, "AES");
    }

    private static IvParameterSpec createIV(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(password);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }

    public static String desEncrypt(String data) {
        try {

            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);
            //byte[] encrypted1 = Base64.decode(data);
            //byte[] encrypted1 = hex2byte(data);

            //Cipher cipher = Cipher.getInstance(CipherMode);
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original,"UTF-8");

            return originalString.trim();//清除空格
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
