package com.board.board.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {

    public String salt = "toyBoard";

    public String getEncrypt(String pwd, String salt) {

        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update((pwd+salt).getBytes());
            byte[] pwdSalt = md.digest();

            StringBuffer sb = new StringBuffer();
            for (byte b : pwdSalt) {
                sb.append(String.format("%02x", b));
            }

            result = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
