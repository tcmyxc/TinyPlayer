package com.tcmyxc.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author : 徐文祥
 * @date : 2021/10/7 20:20
 * @description : MD5Util
 */
public class MD5Util {

    private static String toHexString(byte[] paramArrayOfByte) {
        StringBuilder localStringBuilder = new StringBuilder();
        int i = paramArrayOfByte.length;
        for (int j = 0; j < i; j++) {
            int k = paramArrayOfByte[j];
            if (k < 0){
                k += 256;
            }
            if (k < 16){
                localStringBuilder.append("0");
            }
            localStringBuilder.append(Integer.toHexString(k));
        }
        return localStringBuilder.toString();
    }

    public static String toMD5(String paramString) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.reset();
            localMessageDigest.update(paramString.getBytes("utf-8"));
            return toHexString(localMessageDigest.digest());
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            throw new RuntimeException(localNoSuchAlgorithmException);
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            localUnsupportedEncodingException.printStackTrace();
        }
        return "";
    }
}
