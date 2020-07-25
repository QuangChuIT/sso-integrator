package com.bsc.sso.authentication.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class cryptWithMD5Util {
    private static MessageDigest md;

    public static String cryptWithMD5(String pass) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("MD5");
        byte[] passBytes = pass.getBytes();
        md.reset();
        byte[] digested = md.digest(passBytes);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digested.length; i++) {
            sb.append(Integer.toHexString(0xff & digested[i]));
        }
        return sb.toString();
    }
}
