package com.alkaid.base.extern.security;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.alkaid.base.common.IOUtil;

public class DESCoder {
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String decodeSingleLine(String content, String password)
			throws GeneralSecurityException, UnsupportedEncodingException {
		Cipher localCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec spec = new DESKeySpec(password.substring(0, 8).getBytes());
		SecretKey key = SecretKeyFactory.getInstance("DES")
				.generateSecret(spec);
		IvParameterSpec ivSpec = new IvParameterSpec(password.substring(0, 8)
				.getBytes());
		localCipher.init(2, key, ivSpec);
		return new String(localCipher.doFinal(hexStringToBytes(content)),
				"UTF-8");
	}
	
	public static String decodeMultiLine(String content, String password) throws IOException, GeneralSecurityException{
		BufferedReader r=IOUtil.getBufferReader(content);
		StringBuilder strb = new StringBuilder("");
		int i = 1;
		String line = null;
		try {
			while ((line = r.readLine()) != null) {
				strb.append(decodeSingleLine(line, password)).append("\n");
				i++;
			}
			return strb.toString();
		} catch (EOFException e) {
//			throw e;
		} finally{
			r.close();
		}
		return null;
	}

/*  public static String encrypt(String paramString1, String paramString2)
  {
    try
    {
      Cipher localCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
      byte[] arrayOfByte1 = paramString2.substring(0, 8).getBytes();
      DESKeySpec localDESKeySpec = new DESKeySpec(arrayOfByte1);
      SecretKey localSecretKey = SecretKeyFactory.getInstance("DES").generateSecret(localDESKeySpec);
      byte[] arrayOfByte2 = paramString2.substring(0, 8).getBytes();
      IvParameterSpec localIvParameterSpec = new IvParameterSpec(arrayOfByte2);
      localCipher.init(1, localSecretKey, localIvParameterSpec);
      paramString2 = new StringBuilder();
      byte[] arrayOfByte3 = paramString1.getBytes("UTF-8");
      byte[] arrayOfByte4 = localCipher.doFinal(arrayOfByte3);
      int i = arrayOfByte4.length;
      int j = 0;
      while (true)
      {
        if (j >= i)
        {
          paramString1 = paramString2.toString().toUpperCase();
          return paramString1;
        }
        byte b = arrayOfByte4[j];
        Object[] arrayOfObject = new Object[1];
        Byte localByte = Byte.valueOf(b);
        arrayOfObject[0] = localByte;
        String str = String.format("%02X", arrayOfObject);
        StringBuilder localStringBuilder = paramString2.append(str);
        j += 1;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        localException.printStackTrace();
        paramString1 = "";
      }
    }
  }*/
}