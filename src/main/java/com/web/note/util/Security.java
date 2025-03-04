package com.web.note.util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public class Security {
    private static final SecureRandom random = new SecureRandom();


    public static String generateSecureId() {
//        Random random = new Random();
//        byte[] bytes = new byte[64 / 2];
//        random.nextBytes(bytes);
//
//        MessageDigest digest;
//        try {
//            digest = MessageDigest.getInstance("SHA-256");
//            byte[] hashedBytes = digest.digest(bytes);
//            return bytesToHexString(hashedBytes).toUpperCase();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
        // 生成一个128位的随机数
        BigInteger bigInteger = new BigInteger(256, random);
        StringBuilder hexString = new StringBuilder(bigInteger.toString(16).toUpperCase());

        while (hexString.length() < 64) {
            hexString.insert(0, "0");
        }

        return hexString.substring(0, 64);

    }

    private static String getSha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            return bytesToHexString(hash).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static String bytesToHexString(byte []data){
        StringBuilder hexString = new StringBuilder();
        for (byte b : data) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String passwordHash(String password, String userId) {
        if (userId.length() != 32) {
            throw new RuntimeException("内部严重错误!");
        }

        String shaPassword = getSha256(password);
        String userIdUpperCase = userId.toUpperCase();

        StringBuilder result1 = new StringBuilder();
        for (int i = 0; i < 32; i += 2) {
            char digit = userIdUpperCase.charAt(i);
            result1.append(digit);
        }
        StringBuilder result2 = new StringBuilder();
        for (int i = 1; i < 32; i += 2) {
            char digit = userIdUpperCase.charAt(i);
            result2.append(digit);
        }

        char[] cid1 = result1.toString().toCharArray();
        char[] cid2 = result2.toString().toCharArray();
        int[] par = hexStringToIntArray(shaPassword);

        StringBuilder stringBuilder1 = new StringBuilder();
        for (int j : par) {
            stringBuilder1.append(cid1[j]);
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int j : par) {
            stringBuilder2.append(cid2[j]);
        }

        String salt1 = stringBuilder1.toString();
        String salt2 = stringBuilder2.toString();
        String salt3 = result1.toString();
        String salt4 = result2.toString();

        String saltAndPassword = salt1 + salt3 + shaPassword + salt2 + salt4;

        return getSha256(saltAndPassword);
    }

    private static int[] hexStringToIntArray(String hexString) {
        int[] result = new int[64];

        for (int i = 0; i < 64; i++) {
            char hexChar = hexString.charAt(i);
            int digit = Integer.parseInt(String.valueOf(hexChar),16);

            result[i] = digit;
        }
        return result;
    }

    public static String getUid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String calculateMD5(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            byte[] dataBytes = new byte[1024];

            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte mdbyte : mdbytes) {
                sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
            }
            fis.close();
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Could not calculate MD5", e);
        }
    }
    public static String base64Encode(String input) {
        byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
    public static String escapeSQLike(String data){
        if (data == null) {
            return null;
        }
        String tData=data;
        tData=tData.replaceAll("\\\\","\\\\\\\\"); // 先替换 \ 为 \\
        tData=tData.replaceAll("%","\\\\%");       // 再替换 % 为 \%
        tData=tData.replaceAll("_","\\\\_");       // 最后替换 _ 为 \_
        return tData;
    }


}
