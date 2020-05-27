package com.pjj.utils;


import com.pjj.utils.aes.BASE64Decoder;
import com.pjj.utils.aes.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class ASEUtil {
    /*
     * 加密
     * 1.构造密钥生成器
     * 2.根据ecnodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String AESEncode(String encodeRules, String content) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(encodeRules.getBytes());
            keygen.init(128, secureRandom);
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode = new String(new BASE64Encoder().encode(byte_AES));
            //11.将字符串返回
            return AES_encode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }

    /*
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String AESDncode(String encodeRules, String content) {
        try {
           /* //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG") ;
            secureRandom.setSeed(encodeRules.getBytes());
            keygen.init(128, secureRandom);
            //3.产生原始对称密钥
            SecretKey original_key=keygen.generateKey();*/
            //4.获得原始对称密钥的字节数组
            //byte [] raw=original_key.getEncoded();
            byte[] raw = {94, 82, 45, 84, 61, 32, 9, 2, -67, 51, 88, -37, 6, 60, 79, -83};
            System.out.println(Arrays.toString(raw));
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
             */
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            return AES_decode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }

    public static void main(String[] args) {
//        Scanner scanner=new Scanner(System.in);
//        /*
//         * 加密
//         */
//        System.out.println("使用AES对称加密，请输入加密的规则");
//        String encodeRules=scanner.next();
//        System.out.println("请输入要加密的内容:");
//        String content = scanner.next();
//        System.out.println("根据输入的规则"+encodeRules+"加密后的密文是:"+AESEncode(encodeRules, content));
//
//        /*
//         * 解密
//         */
//        System.out.println("使用AES对称解密，请输入加密的规则：(须与加密相同)");
//        encodeRules=scanner.next();
//        System.out.println("请输入要解密的内容（密文）:");
//        content = scanner.next();
//        System.out.println("根据输入的规则"+encodeRules+"解密后的明文是:"+AESDncode(encodeRules, content));
        String pjjkj = AESDncode("pjjkj", "bzKzH0MMm8V9p8PJEEKVcf9DMA6/1+1VU5nTKYO+p38Ve/y52SCrzmyRlmGOmPo6UbdQZr5eUrRD\nq2nbqFl9s6Fjxwy1rJcz4R/A2UYmfYfz58hIQA8VR4uRdnHYMfFjYCPjuZbuwtK2HMHwlBD8dDqP\nrhuzyPAd83SbJe6a7ER+ilH/ysZaZpOJIUXj/DD9q6GGv8oSxJVrQ4Ku7Cp5FSlaxJJI/P5nWfFO\nh7TPdguCN86OqSGGZtz95eh/V480Ixt8iXa/mOdgrX54MaCVG/IUY77cA2FxYVB3TRJvfnFtSEK+\nerMQyb0UY/v/W2jggGwGxAt0r21HEUoa4N/Mf7IBlnkLUd3f20iJXMKcyF1FO4g40VXtQhgAmytW\nA7W6ens9aMJA3mBioEWEwRlEwSDQUBK2lFzCYCMlp6MzHmUUii++jB/s4f4rhMhJCOO0Wm0S3Kmr\nkvDyntl7HiGRKWw3dy+FN4QJJeohLqOgBoiwc0IaXemdTFIJC7xel1O/MKzqhM8q4K9Rh9iExYDq\nCafqFa4a6XXzSmFIM9h77AjQphzJt626BLth1OG4gbTcFVxnPB2ibWlgMD3VXVGqKPXTjFX+1Jkk\nbIkFMlfXUvCxsWt2YdbzzY6tKNqlQtUnYrtdURBhT1Kf0wuPR6xvIP8rm4M7oD3BlKyB9kvzQd1R\nZCtpHHOm7hnFK8XQEK7bdjp4WOZmKLQ6tmoZ60xH3WgCrLpDPpmGyJRNg0hjZ5W+oXDc8G88/Hdo\nFXx7TvaZzgiBq/IDaqDv7qziWjtZbB8ZtNBjweS+/Y9l9q9Icknu9hSQO9f8dw1hedDnzAn6BGZ2\nr7AFXGjnaWUDcNVbABYsMpF0M6YdYN1XXXdIDzAP6JDpU4u4bxCvKO9owKGDmXW6y3mQiwmJYA93\nObRjK8iB1f1Ccfnhzzxkb+x2Gpt3LrGvFKeYJUqOPQofk/0nLozxE9Pwl/xhlE2RXHrTcgWlWS4q\nh0fbM60YdJJ8FL7RNVQF6qBSbBtHoKcMFEUmOtNH8eOncvQR9oe5bN59ULQkbKNtJwNXBSVrj5AX\ntbKrhDpo9vWcBVTYa3GOpWj6");
        System.out.printf(pjjkj);

    }

}
