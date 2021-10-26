package com.imooc.mall.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 创建者李时汇
 * s h a n L i a n g
 * 创建时间2021/10/24日 7:44
 * ----------------------
 * ---------类描述--------
 * md5是无法反推的加密
 **/
public class MD5Utils {
    public static String getMd5Str(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(str.getBytes(StandardCharsets.UTF_8));
       return Base64.encodeBase64String(digest);

    }

//    public static void main(String[] args) throws NoSuchAlgorithmException {
//        String md5Str = getMd5Str("wo11");
//        System.out.println(md5Str);
//    }
}
