package com.ft.ever.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	/**
	 * 获取二进制数组的MD5值
	 * @param bytes
	 * @return
	 */
	public static String generate(byte[] bytes) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes);
			byte[] digest = md.digest();
			return bytes2String(digest);
		} catch (NoSuchAlgorithmException neverhappen) {
			return null;
		}
	}

	/**
	 * 获取文件的MD5值
	 * @param file
	 * @return
	 */
	public static String checkSum(File file) {
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] dataBytes = new byte[1024];
			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			;
			fis.close();
			return bytes2String(md.digest());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
					fis = null;
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * MD5的16进制转字符串
	 * @param digest
	 * @return
	 */
	private static String bytes2String(byte[] digest) {
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			String di = Integer.toHexString((int) (b & 0xff));
			if(di.length() == 1) sb.append("0");
			sb.append(di);
		}
		return sb.toString();
	}
}
