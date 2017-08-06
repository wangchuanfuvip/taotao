/**
 * MD5.java
 * 
 * Copyright(C)2008 Founder Corporation.
 * written by Founder Corp.
 */
package com.taotao.sso.utils;

import java.security.MessageDigest;

/**
 * [类名]<br>
 * MD5<br>
 * [功能概要]<br>
 * <br>
 * <br>
 * [変更履歴]<br>
 * 2009-3-16 ver1.00 新建 zhaoxinsheng<br>
 * 
 * @author FOUNDER CORPORATION
 * @version 1.00
 */
public class MD5 {
	
	private String inStr;

	private MessageDigest md5;

	/* 下面是构造函数 */
	public MD5(String inStr) {
		this.inStr = inStr;
		try {
			this.md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
    public static String getMD5(String source) {
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte tmp[] = md.digest(); // MD5 的计算结果是�?�� 128 位的长整数，
            // 用字节表示就�?16 个字�?
            char str[] = new char[16 * 2]; // 每个字节�?16 进制表示的话，使用两个字符，
            // �?��表示�?16 进制�?�� 32 个字�?
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第�?��字节�?��，对 MD5 的每�?��字节
                // 转换�?16 进制字符的转�?
                byte byte0 = tmp[i]; // 取第 i 个字�?
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中�?4 位的数字转换,
                // >>> 为�?辑右移，将符号位�?��右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中�?4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符�?

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    
    
    //万里通MD5算法
	/* 下面是关键的md5算法 */
	public String compute() {

		char[] charArray = this.inStr.toCharArray();

		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = this.md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
    
    public static void main(String[] args) {
        System.out.println(MD5.getMD5(MD5.getMD5("111111")+"6ef5fe"));
    }
}

