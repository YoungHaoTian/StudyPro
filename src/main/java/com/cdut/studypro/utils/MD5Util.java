package com.cdut.studypro.utils;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 23:03
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
    public static void main(String[] args){
        String string = MD5Util.stringToMD5("123456");
        System.out.println(string);
        //e10adc3949ba59abbe56e057f20f883e
        //e10adc3949ba59abbe56e057f20f883e
    }
}
