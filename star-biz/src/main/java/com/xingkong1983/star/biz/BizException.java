
package com.xingkong1983.star.biz;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	/**
	 * 错误编码
	 */
	private int code;

	/**
	 * 操作信息
	 */
	private String message;
	/**
	 * 错误日志
	 */
	private String log;

	/**
	 * 数据
	 */
	private Object data;




	/**
	 * 构造函数
	 * @param message 消息
	 */
	public BizException(String message) {
		super(message);
		this.code = 500;
		this.message = message;
	}



	/**
	 * 构造函数
	 * @param code 编码
	 * @param message 消息
	 */
	public BizException(String message, Object data) {
		super(message);
		this.code = 500;
		this.message = message;
		this.data = data;
	}

	/**
	 * 构造函数
	 * @param code 编码
	 * @param message 消息
	 */
	public BizException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	/**
	 * 构造函数
	 * @param code 编码
	 * @param message 消息
	 * @param data 数据
	 */
	public BizException(int code, String message, Object data) {
		super(message);
		this.code = code;
		this.message = message;
		this.data = data;
	}
}
