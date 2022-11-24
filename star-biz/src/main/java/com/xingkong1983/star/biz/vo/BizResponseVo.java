package com.xingkong1983.star.biz.vo;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.xingkong1983.star.biz.BizException;
import com.xingkong1983.star.core.print.IPrint;
import com.xingkong1983.star.core.tool.OsTool;

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

	public static BizResponseVo ok( String message ) {
		BizResponseVo resVo = new BizResponseVo(200, message, "", null);
		return resVo;
	}

	public static BizResponseVo ok( Object data ) {
		BizResponseVo resVo = new BizResponseVo(200, "ok.", "", data);
		return resVo;
	}

	public static BizResponseVo ok( String message, Object data ) {
		BizResponseVo resVo = new BizResponseVo(200, message, "", data);
		return resVo;
	}

	public static BizResponseVo error( String message ) {
		BizResponseVo resVo = new BizResponseVo(500, message, "", null);
		return resVo;
	}

	public static BizResponseVo error( Object data ) {
		BizResponseVo resVo = new BizResponseVo(500, "err.", "", data);
		return resVo;
	}

	public static BizResponseVo error( String message, Object data ) {
		BizResponseVo resVo = new BizResponseVo(500, message, "", data);
		return resVo;
	}

	public static BizResponseVo error( BizException e ) {

		BizResponseVo resVo = new BizResponseVo(e.getCode(), e.getMessage(), OsTool.getErrorText(e), null);
		return resVo;
	}

	public static BizResponseVo error( Object data, BizException e ) {
		BizResponseVo resVo = new BizResponseVo(500, "err.", OsTool.getErrorText(e), data);
		return resVo;
	}

	public BizResponseVo(int code) {
		this.code = code;
		this.data = null;
		this.log = "";
		this.message = "";
		this.time = new Date();
	}

	public BizResponseVo(int code, String message) {
		this.code = code;
		this.message = message;
		this.log = "";
		this.data = null;
		this.time = new Date();
	}

	public BizResponseVo(int code, Object data) {
		this.code = code;
		this.data = data;
		this.log = "";
		this.message = "";
		this.time = new Date();
	}

	public BizResponseVo(int code, String message, Object data) {
		this.code = code;
		this.data = data;
		this.log = "";
		this.message = message;
		this.time = new Date();
	}

	public BizResponseVo(int code, String message, String log, Object data) {
		this.code = code;
		this.log = log;
		this.message = message;
		this.data = data;
		this.time = new Date();
	}

	public String toJson( ) {
		String simpleName = this.getClass().getSimpleName();
		String className = this.getClass().getName();
		String json = JSON.toJSONString(this, true);
		return "\r\n" + simpleName + " (" + className + "):\r\n" + json + log;
	}

}
