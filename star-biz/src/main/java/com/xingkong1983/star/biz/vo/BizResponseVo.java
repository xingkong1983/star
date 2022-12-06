package com.xingkong1983.star.biz.vo;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.xingkong1983.star.core.print.IPrint;

import lombok.Data;

@Data
public class BizResponseVo implements IPrint {
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
	@JSONField(serialize = false)
	private String log;

	/**
	 * 数据
	 */
	private Object data;

	/**
	 * 返回时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date time;

	/**
	 * 构造函数
	 * @param code 代码
	 */
	public BizResponseVo(int code) {
		this.code = code;
		this.data = null;
		this.log = "";
		this.message = "";
		this.time = new Date();
	}

	/**
	 * 构造函数
	 * @param code 代码
	 * @param message 消息
	 */
	public BizResponseVo(int code, String message) {
		this.code = code;
		this.message = message;
		this.log = "";
		this.data = null;
		this.time = new Date();
	}

	/**
	 * 构造函数
	 * @param code 代码
	 * @param message 消息
	 * @param data 数据
	 */
	public BizResponseVo(int code, String message, Object data) {
		this.code = code;
		this.data = data;
		this.log = "";
		this.message = message;
		this.time = new Date();
	}

	/**
	 * 构造函数
	 * @param code 代码
	 * @param message 消息
	 * @param log 日志
	 * @param data 数据
	 */
	public BizResponseVo(int code, String message, String log, Object data) {
		this.code = code;
		this.log = log;
		this.message = message;
		this.data = data;
		this.time = new Date();
	}
	
	/**
	 * 成功
	 * @param message 消息
	 * @return
	 */
	public BizResponseVo success(String message) {
		return error(message,null);
	}
	
	/**
	 * 成功
	 * @param message  消息
	 * @param data
	 * @return
	 */
	public BizResponseVo success(String message, Object data) {
		this.message = message;
		this.data = data;
		return this;
	}
	

	public BizResponseVo error(String message) {
		return error(message,null);
	}
	
	public BizResponseVo error(String message, Object data) {
		this.message = message;
		this.data = data;
		return this;
	}
	
	public BizResponseVo error(String message, Exception e) {
		this.message = message;
		this.log = e.getMessage();
		return this;
	}

	public String toJson() {
		String simpleName = this.getClass().getSimpleName();
		String className = this.getClass().getName();
		String json = JSON.toJSONString(this, true);
		return "\r\n" + simpleName + " (" + className + "):\r\n" + json + log;
	}

}
