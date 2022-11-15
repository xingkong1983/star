package com.xingkong1983.star.core.tool;

import java.util.Random;
import java.util.UUID;

public class IDTool {

	public final static int SIX = 6;
	public final static int FOUR = 4;

	/**
	 * 生成没有-符号的UUID
	 * 
	 * @return
	 */
	public static String genUUID( ) {
		String id = UUID.randomUUID().toString().replace("-", "");
		return id;
	}

	/**
	 * 用于生成6位字符的盐
	 * 
	 * @return
	 */
	public static String genUUIDSALT( ) {
		String id = UUID.randomUUID().toString().replace("-", "");
		return id.substring(0, 6);
	}

	/**
	 * 用于生成唯一的错误编码，方便日志进行搜索
	 * 
	 * @return
	 */
	public static String genErrorID( ) {
		String id = "ERR:" + UUID.randomUUID().toString().replace("-", "");
		return id;
	}

	/**
	 * 生成6位数字编码
	 * 
	 * @return
	 */
	public static int genSixCode( ) {
		return genNumCode(SIX);
	}

	/**
	 * 生成4位数字编码
	 * 
	 * @return
	 */
	public static int genFourCode( ) {
		return genNumCode(FOUR);
	}

	/**
	 * 生成任意长度的数字编码
	 * 
	 * @param num
	 * @return
	 */
	public static int genNumCode( int num ) {
		// 生成随机类
		Random random = new Random();
		int code = 0;
		for (int i = 0; i < num; i++) {
			code = (10 * code) + (random.nextInt(9) + 1);
		}
		// log.info("[num] " + num + "[curCode] " + code);
		return code;
	}
}
