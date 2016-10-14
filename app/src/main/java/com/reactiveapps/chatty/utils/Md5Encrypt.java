package com.reactiveapps.chatty.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Encrypt {

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String md5(String text) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes("utf-8"));

		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");

		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	public static byte[] MD5digestStr(String str) {
		MessageDigest md5Object;
		byte[] digestByte = new byte[16];
		try {
			md5Object = MessageDigest.getInstance("MD5");
			digestByte = md5Object.digest(str.getBytes());
		} catch (NoSuchAlgorithmException e) {
		}
		return digestByte;
	}

	public static String byte2hexString(byte[] bytes) {
		String convertStr = "";
		for (int i = 0; i < bytes.length; i++) {
			String s = Integer.toHexString(bytes[i] & 0x000000ff);
			if (s.length() == 1)
				convertStr += "0";
			convertStr += s;
		}
		return convertStr;
	}

}