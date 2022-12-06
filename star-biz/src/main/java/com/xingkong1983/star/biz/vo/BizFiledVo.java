package com.xingkong1983.star.biz.vo;

import lombok.Data;

@Data
public class BizFiledVo {

	/**
	 * 字段名称
	 */
	String filedName;

	/**
	 * 消息
	 */
	String message;

	/**
	 * 
	 * @param filedName 字段名称
	 * @param message   消息
	 */
	public BizFiledVo(String filedName, String message) {
		this.filedName = filedName;
		this.message = message;
	}

}