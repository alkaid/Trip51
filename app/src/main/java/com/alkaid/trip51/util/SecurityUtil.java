package com.alkaid.trip51.util;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.base.widget.App;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by alkaid on 2015/11/22.
 */
public class SecurityUtil {
    private static byte[] encryptMD5(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            LogUtil.e(e);
        }
        return bytes;
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    private static byte[] SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return sha1hash;
    }

    public static String getMD5WithSalt(String data) {
        String salt="3de8d0f8079b1dc3e4397b8c540823e8";
        if(App.accountService().isLogined()){
            salt=App.accountService().getOpenInfo().getAccesstoken();
        }
        data=salt+data+salt;
        try {
            return byte2hex(encryptMD5(data));
        } catch (IOException e) {
            LogUtil.e(e);
        }
        return null;
    }

    public static String getSHA1WithSalt(String data){
        String salt="ded752ab4704c793e12545bcc654c098ce2f17f7";
        data=salt+data+salt;
        try {
            return byte2hex(SHA1(data));
        } catch (NoSuchAlgorithmException e) {
            LogUtil.e(e);
        } catch (UnsupportedEncodingException e) {
            LogUtil.e(e);
        }
        return null;
    }

}
