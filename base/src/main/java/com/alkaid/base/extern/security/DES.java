package com.alkaid.base.extern.security;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class DES {
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	public static String encode(String s, String pwd) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        IvParameterSpec ivparameterspec = new IvParameterSpec(iv);
        SecretKeySpec secretkeyspec = new SecretKeySpec(pwd.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretkeyspec, ivparameterspec);
        return new String( Base64.encode(cipher.doFinal(s.getBytes()),Base64.DEFAULT) );
    }

	  public static String decode(String s, String pwd) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	    {
	        byte abyte0[] = Base64.decode(s,Base64.DEFAULT);
	        IvParameterSpec ivparameterspec = new IvParameterSpec(iv);
	        SecretKeySpec secretkeyspec = new SecretKeySpec(pwd.getBytes(), "DES");
	        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	        cipher.init(Cipher.DECRYPT_MODE, secretkeyspec, ivparameterspec);
	        return new String(cipher.doFinal(abyte0));
	    }
	  
}
