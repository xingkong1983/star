package com.xingkong1983.star.core.tool;

import java.time.LocalDateTime;

public class StringTool {

	private final static String NULL_STR = "null";


	/**
	 * 文本是 null, "", "Null", "null" 等无意义的字符
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isEmpty( String text ) {
		if (text == null || text.trim().length() == 0 || NULL_STR.toLowerCase().equals(text.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 文本非空
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isNotEmpty( String text ) {
		return !isEmpty(text);
	}

	public static boolean isNotEmpty(LocalDateTime date ) {
		if (date == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Description: 驼峰文本转下划线文本
	 * 
	 * @param text
	 * @return
	 */
	public static String toUnderline( String text ) {

		if (text == null || "".equals(text.trim())) {
			return "";
		}

		int len = text.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = text.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append("_");
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	

}
